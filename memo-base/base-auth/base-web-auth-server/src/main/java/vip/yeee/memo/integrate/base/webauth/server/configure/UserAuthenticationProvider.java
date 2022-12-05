package vip.yeee.memo.integrate.base.webauth.server.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.webauth.server.constant.MessageConstant;
import vip.yeee.memo.integrate.base.websecurity.model.SecurityUser;

import javax.annotation.Resource;

/**
 * 自定义认证逻辑，这里可以拿到用户输入的用户名密码，可以做【等保】
 *
 * @author yeeee
 * @since 2022/11/24 14:53
 */
@Slf4j
@Component
public class UserAuthenticationProvider  implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;

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
