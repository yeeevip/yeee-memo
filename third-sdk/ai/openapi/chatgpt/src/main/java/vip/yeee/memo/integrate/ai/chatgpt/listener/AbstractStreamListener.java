package vip.yeee.memo.integrate.ai.chatgpt.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import vip.yeee.memo.integrate.ai.chatgpt.cache.ChatCache;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.model.ChoiceModel;
import vip.yeee.memo.integrate.ai.chatgpt.util.WebSocketUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractStreamListener extends EventSourceListener {

    protected String lastMessage = "";

    private String gptRole = "";
    protected String chatId = "";
    protected String uid = "";


    /**
     * Called when all new message are received.
     *
     * @param message the new message
     */
    @Setter
    @Getter
    protected BiConsumer<String, String> onComplate = (r, s) -> {

    };

    /**
     * Called when a new message is received.
     * 收到消息 单个字
     *
     * @param message the new message
     */
    public abstract void onMsg(String message);

    /**
     * Called when an error occurs.
     * 出错时调用
     *
     * @param throwable the throwable that caused the error
     * @param response  the response associated with the error, if any
     */
    public abstract void onError(Throwable throwable, String response);

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        // do nothing
    }

    @Override
    public void onClosed(EventSource eventSource) {
        // do nothing
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            onComplate.accept(gptRole, lastMessage);
            return;
        }

        ChatResult response = JSON.parseObject(data, ChatResult.class);
        // 读取Json
        List<ChoiceModel> choices = response.getChoices();
        if (choices == null || choices.isEmpty()) {
            return;
        }
        ChatMessage delta = choices.get(0).getDelta();
        String text = delta.getContent();

        if (StrUtil.isBlank(gptRole)) {
            gptRole = delta.getRole();
        }

        if (text != null) {

            lastMessage += text;

            onMsg(text);

        }

    }


    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable throwable, Response response) {

        try {
            log.error("Stream connection error: {}", throwable);

            String responseText = "";

            if (Objects.nonNull(response)) {
                responseText = response.body().string();
            }

            log.error("response：{}", responseText);

            String forbiddenText = "Your access was terminated due to violation of our policies";

            if (StrUtil.contains(responseText, forbiddenText)) {
                log.error("Chat session has been terminated due to policy violation");
                log.error("检测到号被封了");
            }

            String overloadedText = "That model is currently overloaded with other requests.";

            if (StrUtil.contains(responseText, overloadedText)) {
                log.error("检测到官方超载了，赶紧优化你的代码，做重试吧");
            }

            this.onError(throwable, responseText);

        } catch (Exception e) {
            log.warn("onFailure error:{}", e);
            // do nothing

        } finally {
            eventSource.cancel();
        }
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
