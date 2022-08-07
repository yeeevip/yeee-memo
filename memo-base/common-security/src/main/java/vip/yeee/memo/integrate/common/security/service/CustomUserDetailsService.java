package vip.yeee.memo.integrate.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vip.yeee.memo.integrate.common.security.model.Oauth2TokenDTO;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/29 15:07
 */
public interface CustomUserDetailsService extends UserDetailsService{

    UserDetails loadUserByUsername(String username, String userType) throws UsernameNotFoundException;

    Oauth2TokenDTO oauthToken(String username, String password, String userType);

}
