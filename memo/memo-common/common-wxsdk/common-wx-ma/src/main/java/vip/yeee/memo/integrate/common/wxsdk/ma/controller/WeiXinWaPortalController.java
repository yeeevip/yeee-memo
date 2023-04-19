package vip.yeee.memo.integrate.common.wxsdk.ma.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 11:43
 */
@Slf4j
@RestController
public class WeiXinWaPortalController {

    @Resource
    private WxMaService wxMaService;
    @Resource
    private WxMaMessageRouter wxMaMessageRouter;

    @PostMapping(value = "/wa/portal/{appid}", produces = "application/xml; charset=UTF-8")
    public void post(@PathVariable String appid, HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (!this.wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!this.wxMaService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echoStr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echoStr);
            return;
        }

        String encryptType = request.getParameter("encrypt_type");
        WxMaConfig wxMaConfig = this.wxMaService.getWxMaConfig();
        final boolean isJson = Objects.equals(wxMaConfig.getMsgDataFormat(), WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) {
            // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8));
            } else {//xml
                inMessage = WxMaMessage.fromXml(request.getInputStream());
            }

            final WxMaXmlOutMessage outMessage = this.wxMaMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toXml());
                return;
            }

            response.getWriter().write("success");
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(request.getInputStream(), wxMaConfig);
            } else {//xml
                inMessage = WxMaMessage.fromEncryptedXml(request.getInputStream(), wxMaConfig, timestamp, nonce, msgSignature);
            }

            final WxMaXmlOutMessage outMessage = this.wxMaMessageRouter.route(inMessage);
            if (outMessage != null) {
                response.getWriter().write(outMessage.toEncryptedXml(wxMaConfig));
                return;
            }
            response.getWriter().write("success");
            return;
        }

        response.getWriter().println("不可识别的加密类型");
    }

}
