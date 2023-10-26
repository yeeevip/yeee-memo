package vip.yeee.memo.demo.springcloud.webauth.server.biz;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.websecurityoauth2.constant.SecurityUserTypeEnum;
import vip.yeee.memo.base.websecurityoauth2.model.Oauth2TokenVo;
import vip.yeee.memo.demo.springcloud.webauth.server.model.request.UserAuthRequest;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.common.domain.entity.sys.SysUser;
import vip.yeee.memo.common.domain.mapper.sys.SysUserMapper;
import vip.yeee.memo.demo.springcloud.webauth.server.model.vo.UserAuthVo;
import vip.yeee.memo.demo.springcloud.webauth.server.service.CustomUserDetailsService;

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
    private PasswordEncoder passwordEncoder;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private CustomUserDetailsService customUserDetailsService;

    public Void systemUserRegister(UserAuthRequest request) {
        if (!StrUtil.isAllNotBlank(request.getUsername(), request.getPassword())) {
            throw new BizException("用户名或者密码不能为空");
        }
        SysUser save = new SysUser();
        save.setUsername(request.getUsername());
        save.setPassword(passwordEncoder.encode(request.getPassword()));
        sysUserMapper.insert(save);
        return null;
    }

    public UserAuthVo systemUserLogin(UserAuthRequest request) {
        Oauth2TokenVo oauthToken = customUserDetailsService.oauthToken(SecurityUserTypeEnum.SYSTEM_USER.getType(), request.getUsername()
                , request.getPassword());
        UserAuthVo authVo = new UserAuthVo();
        authVo.setToken(oauthToken.getToken());
        authVo.setTokenHead(oauthToken.getTokenHead());
        return authVo;
    }

    public Void userLogout() {
        customUserDetailsService.logout();
        return null;
    }

}
