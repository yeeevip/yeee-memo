package vip.yeee.memo.common.platformauth.server.configure;

import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.util.LogUtils;
import vip.yeee.memo.base.websecurityoauth2.constant.MessageConstant;
import vip.yeee.memo.base.websecurityoauth2.model.SecurityUser;

import javax.annotation.Resource;

/**
 * 自定义认证逻辑，这里可以拿到用户输入的用户名密码，可以做【等保】
 *
 * @author yeeee
 * @since 2022/11/24 14:53
 */
@Component
public class UserAuthenticationProvider  implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;

    private final static Logger log = LogUtils.commonAuthLog();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("自定义认证逻辑, auth = {}", authentication);
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(username);
        log.info("自定义认证逻辑, SecurityUser = {}", userDetails);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
