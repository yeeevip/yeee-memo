package vip.yeee.memo.integrate.common.platformauth.server.service;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.util.LogUtils;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.MessageConstant;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.SecurityUserTypeEnum;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.AuthUser;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.SecurityUser;

import javax.annotation.Resource;
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
    @Resource
    private PasswordEncoder passwordEncoder;
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
                roles.add(AuthConstant.ROLE_PREFIX + SecurityUserTypeEnum.FRONT_USER.getRole());
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

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
