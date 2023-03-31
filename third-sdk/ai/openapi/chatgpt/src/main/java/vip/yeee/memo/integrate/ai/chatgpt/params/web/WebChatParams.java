package vip.yeee.memo.integrate.ai.chatgpt.params.web;

import lombok.Data;

/**
 * web 聊天初始化参数
 */
@Data
//@ApiModel(value = "网页聊天参数")
public class WebChatParams {
//    @ApiModelProperty("会话ID")
    String chatId;

//    @ApiModelProperty("初始化内容")
    String initContent;

}
