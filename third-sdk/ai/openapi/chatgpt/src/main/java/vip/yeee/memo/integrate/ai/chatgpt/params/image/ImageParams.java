package vip.yeee.memo.integrate.ai.chatgpt.params.image;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
//@ApiModel(value = "图片构建参数")
public class ImageParams {

//    @ApiModelProperty(value = "图片描述",example = "一朵红色的花")
    private String prompt;

//    @ApiModelProperty(value = "画布大小",example = "1024x1024",notes = "256x256 | 512x512 | 1024x1024")
    private String size = "1024x1024";

//    @ApiModelProperty(value = "返回格式",example = "url",notes = "url | b64_json")
    private String response_format = "url";

    @Min(1)
//    @ApiModelProperty(value = "结果数",notes = "如果需要返回多个结果可以设置 n > 1",example = "1")
    private Integer n = 1;

//    @ApiModelProperty(value = "当前用户ID",notes = "用于处理滥用行为",example = "匿名用户")
    private String user;
}
