package vip.yeee.memo.integrate.ai.chatgpt.api.stream.chat;

import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatParams;

import java.util.Map;

@Slf4j
@Component
public class StreamChatApi {

    @Autowired
    private OkHttpClient okHttpClient;
    @Value("${openai.chat.host}")
    private String apiHost;

    public void chatCompletions(Map<String, String> headers, ChatParams params, EventSourceListener eventSourceListener) {
        try {
            EventSource.Factory factory = EventSources.createFactory(okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(params);
            Request request = new Request.Builder()
                    .url(apiHost + "/v1/chat/completions")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .headers(Headers.of(headers))
                    .build();
            factory.newEventSource(request, eventSourceListener);

        } catch (Exception e) {
            log.error("请求出错：{}", e);
        }
    }

}
