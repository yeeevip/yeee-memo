package vip.yeee.memo.integrate.ai.chatgpt.params.web;

import lombok.Data;

@Data
//@ApiModel(value = "助理相关信息")
public class AssistantParams {
//    @ApiModelProperty("助理名称")
    String name;

//    @ApiModelProperty("头像地址")
    String avatar;

//    @ApiModelProperty("介绍")
    String intro;
}
