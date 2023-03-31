package vip.yeee.memo.integrate.ai.chatgpt.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vip.yeee.memo.integrate.ai.chatgpt.dto.ChatDTO;
import vip.yeee.memo.integrate.ai.chatgpt.dto.EditDTO;
import vip.yeee.memo.integrate.ai.chatgpt.dto.ImageDTO;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatMessage;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.chat.ChatResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.EditResult;
import vip.yeee.memo.integrate.ai.chatgpt.params.edit.constant.EditModelEnum;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.image.ImageResult;
import vip.yeee.memo.integrate.ai.chatgpt.service.ChatAuthService;
import vip.yeee.memo.integrate.ai.chatgpt.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * chat 聊天相关接口
 */
@Slf4j
@RestController
@RequestMapping("openai")
public class ChatController {
    @Autowired
    ChatService chatService;

    @Autowired
    ChatAuthService authService;


    @PostMapping("chat")
//    @ApiOperation(value = "聊天",notes = "")
    public Object doChat(@RequestBody ChatDTO dto){
        ChatParams params = new ChatParams();

        List<ChatMessage> messages = chatService.getContext(dto.getChatId(),dto.getWithContext());
        messages.add(chatService.buildUserMessage(dto.getContent()));
        params.setMessages(messages);
        // 执行
        ChatResult result = chatService.doChat(params, dto.getChatId());

        return chatService.simpleResult(result);
    }

    @GetMapping("sse/chat")
    @CrossOrigin
    public SseEmitter doSseChat(ChatDTO dto){
        ChatParams params = new ChatParams();

        List<ChatMessage> messages = chatService.getContext(dto.getChatId(),dto.getWithContext());
        messages.add(chatService.buildUserMessage(dto.getContent()));
        params.setMessages(messages);

        return chatService.doSseChat(params, dto.getChatId());
    }

    @PostMapping("chat/str")
//    @ApiOperation(value = "聊天STR",notes = "")
    public Object doChatStr(@RequestBody ChatDTO dto){
        ChatParams params = new ChatParams();

        List<ChatMessage> messages = chatService.getContext(dto.getChatId(),dto.getWithContext());
        messages.add(chatService.buildUserMessage(dto.getContent()));
        params.setMessages(messages);
        // 执行
       String result = chatService.doChatStr(params, dto.getChatId());
       return result;
    }

    @PostMapping("edit/text")
//    @ApiOperation(value = "文本编辑",notes = "")
    public Object doEditText(@Validated @RequestBody EditDTO dto){
        EditParams params = new EditParams();
        params.setInput(dto.getContent());
        params.setInstruction(dto.getTips());
        params.setModel(EditModelEnum.TEXT.getModel());
        EditResult result = chatService.doEdit(params);
        return chatService.simpleResult(result);
    }

    @PostMapping("edit/code")
//    @ApiOperation(value = "代码编辑",notes = "")
    public Object doEditCode(@Validated @RequestBody EditDTO dto){
        EditParams params = new EditParams();
        params.setInput(dto.getContent());
        params.setInstruction(dto.getTips());
        params.setModel(EditModelEnum.CODE.getModel());
        EditResult result = chatService.doEdit(params);
        return chatService.simpleResult(result);
    }

    @PostMapping("draw")
//    @ApiOperation(value = "图像绘制",notes = "")
    public Object doDraw(@Validated @RequestBody ImageDTO dto){
        ImageParams params = new ImageParams();
        // 设置描述
        params.setPrompt(dto.getTips());
        // 设置返回结果个数
        params.setN(dto.getN());
        ImageResult result = chatService.doDraw(params);
        return chatService.simpleResult(result);
    }
}
