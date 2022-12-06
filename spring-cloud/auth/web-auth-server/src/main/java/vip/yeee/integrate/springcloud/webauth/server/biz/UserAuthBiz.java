package vip.yeee.integrate.springcloud.webauth.server.biz;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.integrate.springcloud.webauth.server.feignclient.WebAuthResource1FeignClient;
import vip.yeee.integrate.springcloud.webauth.server.model.request.UserAuthRequest;
import vip.yeee.integrate.springcloud.webauth.server.model.vo.UserAuthVo;
import vip.yeee.integrate.springcloud.webauth.server.service.UserAuthService;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.SecurityUserTypeEnum;
import vip.yeee.memo.integrate.common.domain.entity.sys.SysUser;
import vip.yeee.memo.integrate.common.domain.mapper.sys.SysUserMapper;
import vip.yeee.memo.integrate.base.websecurityoauth2.context.SecurityContext;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.AuthedUser;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.Oauth2TokenVo;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/17 13:18
 */
@Slf4j
@Component
public class UserAuthBiz {

    @Resource
    private UserAuthService userAuthService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private WebAuthResource1FeignClient webAuthResource1FeignClient;

    public Void systemUserRegister(UserAuthRequest request) {
        if (!StrUtil.isAllNotBlank(request.getUsername(), request.getPassword())) {
            throw new BizException("用户名或者密码不能为空");
        }
        SysUser save = new SysUser();
        save.setUsername(request.getUsername());
        save.setPassword(userAuthService.decodePassword(request.getPassword()));
        sysUserMapper.insert(save);
        return null;
    }

    public UserAuthVo systemUserLogin(UserAuthRequest request) {
        Oauth2TokenVo oauthToken = userAuthService.getUserAccessToken(request.getUsername()
                , request.getPassword()
                , SecurityUserTypeEnum.SYSTEM_USER.getType());
        UserAuthVo authVo = new UserAuthVo();
        authVo.setToken(oauthToken.getToken());
        authVo.setTokenHead(oauthToken.getTokenHead());
        return authVo;
    }

    public Void userLogout() {
        userAuthService.userLogout(SecurityContext.getCurToken());
        return null;
    }

    public String accessSystemApi() {
        AuthedUser curUser = SecurityContext.getCurUser();
        return StrUtil.format("id = {}, username = {}, 访问接口成功", curUser.getId(), curUser.getUsername());
    }

    public String anonymousApiByAnno() {
        CommonResult<String> commonResult = webAuthResource1FeignClient.anonymousApiByAnno();
        log.info("【调用resource1】res = {}", commonResult);
        return "访问【注解标记匿名】接口成功";
    }

    public String anonymousApiByLimit() {
        return "访问【配置限制匿名】接口成功";
    }

}
