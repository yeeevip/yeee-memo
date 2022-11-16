package vip.yeee.memo.integrate.base.webauth.server.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import vip.yeee.memo.integrate.common.websecurity.constant.AuthConstant;
import vip.yeee.memo.integrate.base.webauth.server.constant.MessageConstant;
import vip.yeee.memo.integrate.common.websecurity.constant.SecurityUserTypeEnum;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.common.websecurity.context.SecurityContext;
import vip.yeee.memo.integrate.common.websecurity.model.AuthUser;
import vip.yeee.memo.integrate.common.websecurity.model.Oauth2TokenVo;
import vip.yeee.memo.integrate.common.websecurity.model.SecurityUser;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 15:34
 */
@Slf4j
public abstract class AbstractCustomUserDetailsService implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private TokenStore tokenStore;
    @Resource
    private OAuth2RestTemplate oAuth2RestTemplate;
    private final static String SPLIT_PATTERN = "##";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split(SPLIT_PATTERN);
        String userType = split[0], realUsername = split[1];
        log.info("loadUserByUsername, userType = {}, username = {}", userType, realUsername);
        SecurityUser securityUser;
        try {
            if (SecurityUserTypeEnum.SYSTEM_USER.getType().equals(userType)) {
                // find user
                AuthUser sysUser = this.getSystemUserByUsername(realUsername);
                Set<String> roles = Optional.ofNullable(sysUser.getRoles()).orElseGet(Sets::newHashSet);
                Set<String> permissions = Optional.ofNullable(sysUser.getPermissions()).orElseGet(Sets::newHashSet);
                roles.add(AuthConstant.ROLE_PREFIX + SecurityUserTypeEnum.SYSTEM_USER.getRole());
                roles.addAll(permissions);
                // build security-user
                securityUser = new SecurityUser(sysUser.getUserId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getState(), roles);
            } else if (SecurityUserTypeEnum.FRONT_USER.getType().equals(userType)) {
                // find user
                AuthUser user = this.getFrontUserByUsername(realUsername);
                Set<String> roles = Optional.ofNullable(user.getRoles()).orElseGet(Sets::newHashSet);
                Set<String> permissions = Optional.ofNullable(user.getPermissions()).orElseGet(Sets::newHashSet);
                roles.add(AuthConstant.ROLE_PREFIX + SecurityUserTypeEnum.SYSTEM_USER.getRole());
                roles.addAll(permissions);
                // build security-user
                securityUser = new SecurityUser(user.getUserId(), user.getUsername(), user.getPassword(), user.getState(), roles);
            } else {
                log.warn("不允许的用户类型");
                throw new BizException(MessageConstant.USER_TYPE_ERROR);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        // check
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }

        return securityUser;
    }

    public abstract AuthUser getSystemUserByUsername(String username);

    public abstract AuthUser getFrontUserByUsername(String username);

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String decodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Oauth2TokenVo getUserAccessToken(String username, String password, String userType) {
        log.info("getUserAccessToken, userType = {}, username = {}, password = {}", userType, username, password);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BizException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        ResourceOwnerPasswordResourceDetails passwordResourceDetails =
                (ResourceOwnerPasswordResourceDetails) this.oAuth2RestTemplate.getResource();
        passwordResourceDetails.setUsername(userType + SPLIT_PATTERN + username);
        passwordResourceDetails.setPassword(password);
        oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
        OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
        Oauth2TokenVo oauth2TokenVo = new Oauth2TokenVo();
        oauth2TokenVo.setToken(accessToken.getValue());
        oauth2TokenVo.setRefreshToken(accessToken.getRefreshToken().getValue());
        oauth2TokenVo.setExpiresIn(accessToken.getExpiresIn());
        oauth2TokenVo.setTokenHead(AuthConstant.JWT_TOKEN_PREFIX);
        return oauth2TokenVo;
    }

    public Object userLogout(String token) {
        log.info("logout");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        // 移除access_token
        tokenStore.removeAccessToken(accessToken);
        // 移除refresh_token
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        return null;
    }

}
