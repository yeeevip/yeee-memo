package vip.yeee.memo.integrate.ai.chatgpt.api.block.edit;

import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.http.ForestResponse;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditResult;

import java.util.Map;

public interface EditApi {
    // 文本或者代码编辑接口
    @Post("#{openai.chat.host}/v1/edits")
    ForestResponse<EditResult> ChatEdits(@Header Map<String, String> headers, @JSONBody EditParams params);
}
