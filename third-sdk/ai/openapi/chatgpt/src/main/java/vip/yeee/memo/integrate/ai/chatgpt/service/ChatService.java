package vip.yeee.memo.integrate.ai.chatgpt.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageResult;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * 使用ChatGpt 相关服务接口
 */
public interface ChatService {
    // 构建请求头
    Map<String,String> headers();

    // 构建用户消息
    ChatMessage buildUserMessage(String content);

    // 构建会话
    ChatResult doChat(ChatParams params);

    // 构建会话 V2 -- 携带会话ID
    ChatResult doChat(ChatParams params, String chatId);

    SseEmitter doSseChat(ChatParams params, String chatId);

    void doWsChat(ChatParams params, String chatId, Session session);

    // Str 形式返回
    String doChatStr(ChatParams params, String chatId);

    // 构建编辑执行
    EditResult doEdit(EditParams params);

    // 构建图形绘制
    ImageResult doDraw(ImageParams params);

    // 获取上下文
    List<ChatMessage> getContext(String chatId);

    // 获取上下文 -- 指定条数
    List<ChatMessage> getContext(String chatId, Integer num);

    // 简化返回结果
    String simpleResult(ChatResult result);

    // 简化编辑返回结果
    String simpleResult(EditResult result);

    // 简化图片绘制返回结果
    List<String> simpleResult(ImageResult result);
}
