package vip.yeee.memo.common.platformauth.server.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.util.LogUtils;
import vip.yeee.memo.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.base.websecurityoauth2.constant.MessageConstant;
import vip.yeee.memo.base.websecurityoauth2.model.AuthUser;
import vip.yeee.memo.base.websecurityoauth2.model.Oauth2TokenVo;
import vip.yeee.memo.base.websecurityoauth2.model.SecurityUser;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 15:34
 */
public abstract class AbstractCustomUserDetailsService implements UserDetailsService {

    private final static Logger log = LogUtils.commonAuthLog();

    @Resource
    private TokenStore tokenStore;
    @Resource
    private OAuth2ProtectedResourceDetails resourceOwnerPasswordResourceDetails;
    @Resource
    private ServerProperties serverProperties;

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
            Set<String> authoritySet = Sets.newHashSet();
            Set<String> roles = Optional.ofNullable(sysUser.getRoles()).orElseGet(Sets::newHashSet);
            roles.add(userType);
            roles = roles.stream()
                    .map(r -> !r.startsWith(AuthConstant.ROLE_PREFIX) ? AuthConstant.ROLE_PREFIX + r : r)
                    .collect(Collectors.toSet());
            authoritySet.addAll(roles);
            Set<String> groups = Optional.ofNullable(sysUser.getGroups()).orElseGet(Sets::newHashSet);
            groups = groups.stream()
                    .map(r -> !r.startsWith(AuthConstant.GROUP_PREFIX) ? AuthConstant.GROUP_PREFIX + r : r)
                    .collect(Collectors.toSet());
            authoritySet.addAll(groups);
            Set<String> permissions = Optional.ofNullable(sysUser.getPermissions()).orElseGet(Sets::newHashSet);
            authoritySet.addAll(permissions);
            // build security-user
            securityUser = new SecurityUser(sysUser.getUserId(), userType, sysUser.getUsername(), sysUser.getPassword(), sysUser.getState(), authoritySet);
            securityUser.setSuperAdmin(Integer.valueOf(1).equals(sysUser.getSuperAdmin()));
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

    public Oauth2TokenVo oauthToken(String userType, String username, String password) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BizException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        try {
            Map<String, Object> params = Maps.newHashMap();
            params.put(AuthConstant.AUTH_CLIENT_ID, resourceOwnerPasswordResourceDetails.getClientId());
            params.put(AuthConstant.AUTH_CLIENT_SECRET, resourceOwnerPasswordResourceDetails.getClientSecret());
            params.put(AuthConstant.AUTH_GRANT_TYPE, resourceOwnerPasswordResourceDetails.getGrantType());
            params.put(AuthConstant.AUTH_USERNAME, userType + AuthConstant.USERNAME_SEPARATOR + username);
            params.put(AuthConstant.AUTH_PASSWORD, password);
            String contextPath = serverProperties.getServlet().getContextPath();
            String url = "http://localhost" + ":" + serverProperties.getPort()
                    + ("/".equals(contextPath) ? "" : contextPath) + "/oauth/token";
            HttpResponse response = HttpRequest.post(url).form(params).execute();
            if (!response.isOk()) {
                log.warn("oauthToken error, response = {}", response);
                throw new Exception(response.toString());
            }
            JSONObject jsonObject = JSON.parseObject(response.body());
            Oauth2TokenVo oauth2TokenVo = new Oauth2TokenVo();
            oauth2TokenVo.setToken(jsonObject.getString("access_token"));
            oauth2TokenVo.setRefreshToken(jsonObject.getString("refresh_token"));
            oauth2TokenVo.setExpiresIn(jsonObject.getInteger("expires_in"));
            oauth2TokenVo.setTokenHead(AuthConstant.JWT_TOKEN_PREFIX);
            return oauth2TokenVo;
        } catch (Exception e) {
            log.error("oauthToken error，userType = {}，username = {}", userType, username, e);
            throw new BizException(MessageConstant.AUTH_ERROR);
        }
    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.logout(authentication);
    }

    public void logout(Authentication authentication) {
        OAuth2AccessToken accessToken = tokenStore.getAccessToken((OAuth2Authentication) authentication);
        if (accessToken != null) {
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);
            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
        }
    }

}
