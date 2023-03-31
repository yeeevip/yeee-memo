package vip.yeee.memo.integrate.ai.chatgpt.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
//@ApiModel(value = "图片参数")
public class ImageDTO {
    @NotBlank(message = "图像描述不可为空")
//    @ApiModelProperty(value = "描述")
    private String tips;

    @Min(1)
//    @ApiModelProperty(value = "绘制结果数",example = "2")
    private Integer n = 1;
}
