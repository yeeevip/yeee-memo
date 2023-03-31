package vip.yeee.memo.integrate.ai.chatgpt.params.edit.model;

import lombok.Data;

@Data
//@ApiModel(value = "编辑返回结果")
public class EditChoiceModel {
//    @ApiModelProperty(value = "结果下标")
    Integer index;

//    @ApiModelProperty(value = "返回内容",example = "hello")
    String text;
}
