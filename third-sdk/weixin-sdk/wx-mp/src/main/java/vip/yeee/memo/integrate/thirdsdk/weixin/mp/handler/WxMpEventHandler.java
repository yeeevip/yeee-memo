package vip.yeee.memo.integrate.thirdsdk.weixin.mp.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.domain.entity.api.ApiUserIdentity;
import vip.yeee.memo.integrate.common.domain.service.api.ApiUserIdentityService;

import java.util.Arrays;
import java.util.Map;

/**
 * description ...
 * @author yeeeeee
 * @since 2022/2/22 11:19
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WxMpEventHandler implements WxMpMessageHandler {

    private final ApiUserIdentityService apiUserIdentityService;

    private final static Integer OPENAPPTYPE_WX_AUTHAPP = 10;

    private final static String WX_AUTHAPP_NOT_BIND_TEMPLATE = "<a href=\"{}\">点击绑定手机号</a>" +
            "\n\n" +
            "温馨提示：请绑定您提供给企业进行授权的手机号（即收到授权通知短信的手机号），绑定后即可收到企业对您发起的XX授权申请通知";

    /**
     * description ...
     * @author yeeeeee
     * @since 2022/2/23 10:10
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        if (!Arrays.asList(WxConsts.EventType.SUBSCRIBE, WxConsts.EventType.UNSUBSCRIBE, WxConsts.EventType.CLICK).contains(wxMessage.getEvent())) {
            return null;
        }
        LambdaQueryWrapper<ApiUserIdentity> wrapper = Wrappers.<ApiUserIdentity>lambdaQuery()
                .eq(ApiUserIdentity::getUserType, 10)
                .eq(ApiUserIdentity::getAppType, OPENAPPTYPE_WX_AUTHAPP)
                .eq(ApiUserIdentity::getOpenId, wxMessage.getFromUser());
        ApiUserIdentity userIdentity = apiUserIdentityService.getOne(wrapper);
        if (WxConsts.EventType.CLICK.equals(wxMessage.getEvent())) {
            if ("GO_AUTH".equals(wxMessage.getEventKey())) {
                //checkSaveUser(userIdentity, wxMessage);
                WxMpXmlOutTextMessage message = applyBindMessage(userIdentity, wxMessage, StrUtil.isBlank(userIdentity.getMobile()));;
                return message;
            }
        } if (WxConsts.EventType.SUBSCRIBE.equals(wxMessage.getEvent())) {
            checkSaveUser(userIdentity, wxMessage);
        } else if (WxConsts.EventType.UNSUBSCRIBE.equals(wxMessage.getEvent())) {
            if (userIdentity != null) {
                apiUserIdentityService.updateById(new ApiUserIdentity().setId(userIdentity.getId()).setStatus(1));
            }
        }

        return null;
    }

    private void checkSaveUser(ApiUserIdentity userIdentity, WxMpXmlMessage wxMessage) {
        if (userIdentity == null) {
            userIdentity = new ApiUserIdentity()
                    .setUserType(10)
                    .setAppType(OPENAPPTYPE_WX_AUTHAPP)
                    .setOpenId(wxMessage.getFromUser())
                    .setStatus(0);
            apiUserIdentityService.save(userIdentity);
        } else {
            apiUserIdentityService.updateById(new ApiUserIdentity().setId(userIdentity.getId()).setStatus(0));
        }
    }

    // oauth2 code重定向地址 -> code换取token从而调用api
    private String oauth2CodeToken(String redirectUri) {
        return StrUtil.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc84b896c35f157b1&redirect_uri={}&response_type=code&scope=snsapi_base&state=&connect_redirect=1#wechat_redirect", URLUtil.encode(redirectUri));
    }

    private WxMpXmlOutTextMessage applyBindMessage(ApiUserIdentity userIdentity, WxMpXmlMessage wxMessage, boolean bind) {
        if (!Integer.valueOf(0).equals(userIdentity.getStatus())) {
            return null;
        }
        WxMpXmlOutTextMessage message = WxMpXmlOutMessage.TEXT()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        String content = StrUtil.format(WX_AUTHAPP_NOT_BIND_TEMPLATE, "https://www.yeee.vip?page=binding&ticket=" + SecureUtil.aes().encryptHex(wxMessage.getFromUser()));
        message.setContent(content);
        return message;
    }

}
