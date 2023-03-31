package vip.yeee.memo.integrate.ai.chatgpt.params.edit;

import vip.yeee.memo.integrate.ai.chatgpt.params.edit.validator.IsEditModel;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
//@ApiModel(value = "编辑参数")
public class EditParams {
    @IsEditModel
//    @ApiModelProperty(value = "模型",example = "text-davinci-edit-001")
    private String model = "text-davinci-edit-001";

    @NotBlank(message = "编辑内容不可为空")
//    @ApiModelProperty(value = "编辑内容",example = "What day of the wek is it?")
    private String input;

    @NotBlank(message = "提示不可为空")
//    @ApiModelProperty(value = "编辑提示",example = "fix the mistakes",notes = "给模型方向提示")
    private String instruction;


    @DecimalMin("0.0")
    @DecimalMax("2.0")
//    @ApiModelProperty(value = "采样温度",notes = "较高的值（如 0.8）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定",example = "1.0")
    private Double temperature = 1.0;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
//    @ApiModelProperty(value = "核心采样",notes = "其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的结果集",example = "1.0")
    private Double top_p = 1.0;

    @Min(1)
//    @ApiModelProperty(value = "结果数",notes = "如果需要返回多个结果可以设置 n > 1",example = "1")
    private Integer n = 1;
}
