package vip.yeee.memo.integrate.ai.chatgpt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.http.ForestResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vip.yeee.memo.integrate.ai.chatgpt.api.block.chat.ChatApi;
import vip.yeee.memo.integrate.ai.chatgpt.api.block.edit.EditApi;
import vip.yeee.memo.integrate.ai.chatgpt.api.block.image.ImageApi;
import vip.yeee.memo.integrate.ai.chatgpt.api.stream.chat.StreamChatApi;
import vip.yeee.memo.integrate.ai.chatgpt.cache.ChatCache;
import vip.yeee.memo.integrate.ai.chatgpt.cache.RedisCache;
import vip.yeee.memo.integrate.ai.chatgpt.constant.ChatRoleConst;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.model.ChoiceModel;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.model.EditChoiceModel;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.model.ImageChoiceModel;
import vip.yeee.memo.integrate.ai.chatgpt.service.ChatAuthService;
import vip.yeee.memo.integrate.ai.chatgpt.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vip.yeee.memo.integrate.ai.chatgpt.listener.SseStreamListener;
import vip.yeee.memo.integrate.ai.chatgpt.listener.WsEventSourceListener;
import vip.yeee.memo.integrate.ai.chatgpt.util.WebSocketUtil;
import vip.yeee.memo.integrate.common.web.sse.util.SseEmitterUtil;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * OpenAI相关服务实现
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Value("${openai.chat.key}")
    private String chatKey;
    @Resource
    private ChatApi chatApi;
    @Resource
    private EditApi editApi;
    @Resource
    private ImageApi imageApi;
    @Autowired
    ChatAuthService authService;
    @Resource
    private StreamChatApi streamChatApi;
    @Resource
    private RedisCache redisCache;

    @Override
    public Map<String, String> headers() {
        Map<String,String> headers = new HashMap<>();
        String apiToken;
        if (StrUtil.isNotBlank(redisCache.getApiToken())) {
            apiToken = redisCache.getApiToken();
        } else {
            apiToken = chatKey;
        }
        headers.put("Authorization","Bearer " + apiToken);
        return headers;
    }

    @Override
    public ChatMessage buildUserMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.setRole(ChatRoleConst.USER);
        message.setContent(content);
        return message;
    }

    @Override
    public ChatResult doChat(ChatParams params) {
        // 写入用户ID
        params.setUser(authService.getUserId());
        ForestResponse<ChatResult> response =  chatApi.ChatCompletions(headers(),params);
        ChatResult result = response.getResult();
        return result;
    }

    @Override
    public ChatResult doChat(ChatParams params, String chatId) {
        // 写入用户ID
        params.setUser(authService.getUserId());
        ForestResponse<ChatResult> response =  chatApi.ChatCompletions(headers(),params);
        ChatResult result = response.getResult();
        // 获取返回结果
        List<ChoiceModel> choices = result.getChoices();
        // 获取第一条结果
        ChoiceModel choice = choices.get(0);
        // 获取消息
        ChatMessage message = choice.getMessage();
        // 写入缓存
        List<ChatMessage> messages = ChatCache.get(chatId);
        messages.add(message);
        ChatCache.put(chatId,messages);
        // 返回结果
        return result;
    }

    @Override
    public SseEmitter doSseChat(ChatParams params, String chatId) {
        SseEmitter sseEmitter = SseEmitterUtil.connect(chatId);
        params.setUser(authService.getUserId());
        params.setStream(true);
        SseStreamListener listener = new SseStreamListener(sseEmitter);
        streamChatApi.chatCompletions(headers(), params, listener);
        return sseEmitter;
    }

    @Override
    public void doWsChat(ChatParams params, String chatId, Session session) {
        WsEventSourceListener listener = new WsEventSourceListener(session);
        listener.setChatId(chatId);
        String uid = WebSocketUtil.getIp(session);
        Integer count = Optional.ofNullable(redisCache.getULimitCountCache(uid)).orElse(0);
        if ((StrUtil.isBlank(redisCache.getULimitExclude())
                || !Arrays.asList(redisCache.getULimitExclude().split(",")).contains(uid))
                && count >= 5) {
            listener.onMsg("今日使用次数已达限制！！！");
            return;
        }
        listener.setOnComplate((r, s) -> {
            // 写入缓存
            List<ChatMessage> messages = ChatCache.get(chatId);
            ChatMessage message = new ChatMessage();
            message.setRole(r);
            message.setContent(s);
            messages.add(message);
            ChatCache.put(chatId,messages);
            redisCache.incrULimitCountCache(uid);
        });
        params.setUser(authService.getUserId());
        params.setStream(true);
        streamChatApi.chatCompletions(headers(), params, listener);
    }

    @Override
    public String doChatStr(ChatParams params, String chatId) {
        params.setUser(authService.getUserId());
        return chatApi.ChatCompletionsStr(headers(),params);
    }

    @Override
    public EditResult doEdit(EditParams params) {
        ForestResponse<EditResult> response =  editApi.ChatEdits(headers(),params);
        EditResult result = response.getResult();
        return result;
    }

    @Override
    public ImageResult doDraw(ImageParams params) {
        // 写入用户
        params.setUser(authService.getUserId());
        ForestResponse<ImageResult> response =  imageApi.ChatDraw(headers(),params);
        ImageResult result = response.getResult();
        return result;
    }

    @Override
    public List<ChatMessage> getContext(String chatId) {
        List<ChatMessage> messages = ChatCache.get(chatId);
        return messages;
    }


    @Override
    public List<ChatMessage> getContext(String chatId, Integer num) {
        List<ChatMessage> messages = getContext(chatId);
        int msgSize = messages.size();

        if (msgSize > num){
            return messages.subList(msgSize - num,msgSize);
        }else {
            return messages;
        }
    }

    @Override
    public String simpleResult(ChatResult result) {
        // 获取返回结果
        List<ChoiceModel> choices = result.getChoices();
        // 获取第一条结果
        ChoiceModel choice = choices.get(0);
        // 获取消息
        ChatMessage message = choice.getMessage();
        // 返回内容
        return message.getContent();
    }

    @Override
    public String simpleResult(EditResult result) {
        List<EditChoiceModel> choices = result.getChoices();
        // 获取结果
        EditChoiceModel choice = choices.get(0);
        // 获取消息
        return choice.getText();
    }

    @Override
    public List<String> simpleResult(ImageResult result) {
        List<ImageChoiceModel> choices = result.getData();
        List<String> res = choices.stream().map(ImageChoiceModel::getUrl).collect(Collectors.toList());
        return res;
    }
}
