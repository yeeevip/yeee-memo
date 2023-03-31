package vip.yeee.memo.integrate.ai.chatgpt.params.edit;

import vip.yeee.memo.integrate.ai.chatgpt.params.edit.model.EditChoiceModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
//@ApiModel(value = "编辑返回结果")
public class EditResult {
//    @ApiModelProperty("chat-id")
    private String id;

//    @ApiModelProperty("调用对象")
    private String object;

//    @ApiModelProperty("创建ID")
    private Long created;


//    @ApiModelProperty("token消耗")
    private Map<String,String> usage;

    // 可选结果集合
//    @ApiModelProperty("可选结果集合")
    List<EditChoiceModel> choices;
}
