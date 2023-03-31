package vip.yeee.memo.integrate.ai.chatgpt.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
//@ApiModel(value = "聊天数据对象")
public class ChatDTO {
    @NotBlank(message = "聊天内容不可为空")
//    @ApiModelProperty("聊天内容")
    String content;

    @Min(0)
//    @ApiModelProperty(value = "携带上下文条数",example = "2")
    Integer withContext;

    @NotBlank(message = "会话ID不可为空")
//    @ApiModelProperty(value = "会话ID",example = "10086")
    String chatId;

}
