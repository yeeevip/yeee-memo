package vip.yeee.memo.common.platformauth.server.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.util.LogUtils;
import vip.yeee.memo.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.base.websecurityoauth2.constant.MessageConstant;
import vip.yeee.memo.base.websecurityoauth2.model.AuthUser;
import vip.yeee.memo.base.websecurityoauth2.model.SecurityUser;

import java.util.Optional;
import java.util.Set;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 15:34
 */
public abstract class AbstractCustomUserDetailsService implements UserDetailsService {

    private final static Logger log = LogUtils.commonAuthLog();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split(AuthConstant.USERNAME_SEPARATOR);
        if (split.length != 2 && StrUtil.isBlank(split[0])) {
            log.warn("不允许的用户类型");
            throw new BizException(MessageConstant.USER_TYPE_ERROR);
        }
        String userType = split[0], realUsername = split[1];
        log.info("loadUserByUsername, userType = {}, username = {}", userType, realUsername);
        SecurityUser securityUser;
        try {
            // find user
            AuthUser sysUser = this.getUserByUserTypeAndUsername(userType, realUsername);
            Set<String> roles = Optional.ofNullable(sysUser.getRoles()).orElseGet(Sets::newHashSet);
            Set<String> permissions = Optional.ofNullable(sysUser.getPermissions()).orElseGet(Sets::newHashSet);
            roles.add(AuthConstant.ROLE_PREFIX + userType);
            roles.addAll(permissions);
            // build security-user
            securityUser = new SecurityUser(sysUser.getUserId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getState(), roles);
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

    public abstract AuthUser getUserByUserTypeAndUsername(String userType, String username);

}
