package vip.yeee.memo.integrate.ai.chatgpt.params.chat.model;

import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;
import lombok.Data;

@Data
//@ApiModel(value = "结果集")
public class ChoiceModel {
//    @ApiModelProperty(value = "结果下标")
    Integer index;

//    @ApiModelProperty(value = "终止原因")
    String finish_reason;


//    @ApiModelProperty(value = "可选结果")
    /**
     * 请求参数stream为false返回是message
     */
    ChatMessage message;

    /**
     * 请求参数stream为true返回是delta
     */
    ChatMessage delta;
}
