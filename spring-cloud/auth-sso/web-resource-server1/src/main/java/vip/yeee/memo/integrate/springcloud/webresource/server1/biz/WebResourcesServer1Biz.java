package vip.yeee.memo.integrate.springcloud.webresource.server1.biz;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.SecurityUserTypeEnum;
import vip.yeee.memo.integrate.base.websecurityoauth2.context.SecurityContext;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.AuthedUser;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.Oauth2TokenVo;
import vip.yeee.memo.integrate.springcloud.webresource.server1.feignclient.WebAuthServerFeignClient;
import vip.yeee.memo.integrate.springcloud.webresource.server1.model.request.UserAuthRequest;
import vip.yeee.memo.integrate.springcloud.webresource.server1.model.vo.UserAuthVo;
import vip.yeee.memo.integrate.springcloud.webresource.server1.service.WebAuthService;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/17 13:18
 */
@Slf4j
@Component
public class WebResourcesServer1Biz {

    @Resource
    private WebAuthServerFeignClient webAuthServerFeignClient;
    @Resource
    private WebAuthService webAuthService;

    public Void systemUserRegister(UserAuthRequest request) {
        return webAuthServerFeignClient.systemUserRegister(request).getData();
    }

    public UserAuthVo systemUserLogin(UserAuthRequest request) {
        Oauth2TokenVo oauthToken = webAuthService.getUserAccessToken(request.getUsername()
                , request.getPassword()
                , SecurityUserTypeEnum.SYSTEM_USER.getType());
        UserAuthVo authVo = new UserAuthVo();
        authVo.setToken(oauthToken.getToken());
        authVo.setTokenHead(oauthToken.getTokenHead());
        return authVo;
    }

    public Void userLogout() {
        webAuthService.userLogout(SecurityContext.getCurToken());
        return null;
    }
    public String accessApi() {
        AuthedUser curUser = SecurityContext.getCurUser();
        return StrUtil.format("id = {}, username = {}, 访问接口成功", curUser.getId(), curUser.getUsername());
    }

    public String anonymousApiByAnno() {
        return "访问【注解标记匿名】接口成功";
    }

    public String anonymousApiByLimit() {
        return "访问【配置限制匿名】接口成功";
    }
}
