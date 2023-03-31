package vip.yeee.memo.integrate.ai.chatgpt.params.image;

import vip.yeee.memo.integrate.ai.chatgpt.params.image.model.ImageChoiceModel;
import lombok.Data;

import java.util.List;

@Data
//@ApiModel(value = "图像编辑结果")
public class ImageResult {
//    @ApiModelProperty("创建ID")
    private Long created;

//    @ApiModelProperty("结果列表")
    private List<ImageChoiceModel> data;
}
