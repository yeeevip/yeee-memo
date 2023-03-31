package vip.yeee.memo.integrate.ai.chatgpt.controller;

import cn.hutool.core.util.IdUtil;
import vip.yeee.memo.integrate.ai.chatgpt.params.web.AssistantParams;
import vip.yeee.memo.integrate.ai.chatgpt.params.web.WebChatParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("chat")
public class WebChatController {
//    @ApiIgnore
    @GetMapping("web")
//    @ApiOperation(value = "在线聊天室",notes = "")
    public ModelAndView webChatRoom(){
        ModelAndView modelAndView = new ModelAndView();
        // 构建助理详细信息
        AssistantParams params = new AssistantParams();
        params.setName("3.5助手");
        params.setAvatar("https://s1.ax1x.com/2023/03/20/ppNV4Fs.png");
        params.setIntro("随时为您服务");
        // 构建聊天初始化信息
        WebChatParams initParams = new WebChatParams();
        initParams.setChatId(IdUtil.fastSimpleUUID());
        initParams.setInitContent("小助手随时为您服务");
        modelAndView.setViewName("chat");
        modelAndView.addObject("assistant",params);
        modelAndView.addObject("init",initParams);
        return modelAndView;
    }

}
