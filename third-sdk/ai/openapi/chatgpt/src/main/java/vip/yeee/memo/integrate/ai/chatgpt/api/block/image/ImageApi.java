package vip.yeee.memo.integrate.ai.chatgpt.api.block.image;

import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.http.ForestResponse;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageResult;

import java.util.Map;

public interface ImageApi {
    // 图像生成接口
    @Post("#{openai.chat.host}/v1/images/generations")
    ForestResponse<ImageResult> ChatDraw(@Header Map<String, String> headers, @JSONBody ImageParams params);

    //ForestResponse<ImageResult> ChatImageEdit(@Header Map<String, Object> headers, @JSONBody ImageParams params);
}
