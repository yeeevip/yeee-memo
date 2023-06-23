package vip.yeee.memo.demo.thirdsdk.weixin.ma.handler;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description ...
 * @author yeeeeee
 * @since 2022/2/22 11:19
 */
@Slf4j
@Component
public class TextWxMaMessageHandler implements WxMaMessageHandler {

    @Override
    public WxMaXmlOutMessage handle(WxMaMessage wxMaMessage, Map<String, Object> map, WxMaService wxMaService, WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("WxMaMessageHandler - 处理文本消息");
        return null;
    }
}
