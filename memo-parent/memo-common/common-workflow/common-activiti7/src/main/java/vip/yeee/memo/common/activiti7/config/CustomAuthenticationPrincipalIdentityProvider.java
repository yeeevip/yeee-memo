package vip.yeee.memo.common.activiti7.config;

import org.activiti.api.runtime.shared.security.PrincipalIdentityProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/12/28 15:09
 */
@Order(Integer.MIN_VALUE)
@Configuration
public class CustomAuthenticationPrincipalIdentityProvider implements PrincipalIdentityProvider {

    private final static String EMPTY_ANONYMOUS_USER_ID = "";

    @Override
    public String getUserId(Principal principal) {
        return Optional.of(principal)
                .filter(Authentication.class::isInstance)
                .map(Authentication.class::cast)
                .map(this::getUserId)
                .orElseThrow(this::securityException);
    }

    protected String getUserId(Authentication authentication) {
        if (authentication.getPrincipal() != null) {
            return (String) ((Map)authentication.getPrincipal()).get("username");
        }
        return this.getAnonymousUserId();
    }

    protected String getAnonymousUserId() {
        return EMPTY_ANONYMOUS_USER_ID;
    }

    protected SecurityException securityException() {
        return new SecurityException("Invalid principal authentication object instance");
    }
}
