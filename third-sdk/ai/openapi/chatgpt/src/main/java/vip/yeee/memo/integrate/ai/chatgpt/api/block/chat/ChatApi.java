package vip.yeee.memo.integrate.ai.chatgpt.api.block.chat;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatResult;

import java.util.Map;

/**
 * 聊天相关API
 */
public interface ChatApi {
    // 新建聊天接口
    @Post("#{openai.chat.host}/v1/chat/completions")
    ForestResponse<ChatResult> ChatCompletions(@Header Map<String, String> headers, @JSONBody ChatParams params);

    @Post("#{openai.chat.host}/v1/chat/completions")
    String ChatCompletionsStr(@Header Map<String, String> headers, @JSONBody ChatParams params);
}
