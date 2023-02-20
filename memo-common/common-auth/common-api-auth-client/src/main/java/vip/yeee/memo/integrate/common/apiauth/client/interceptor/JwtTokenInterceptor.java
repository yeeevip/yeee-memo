package vip.yeee.memo.integrate.common.apiauth.client.interceptor;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.yeee.memo.integrate.base.util.LogUtils;
import vip.yeee.memo.integrate.common.apiauth.client.context.ApiSecurityContext;
import vip.yeee.memo.integrate.common.apiauth.client.kit.JwsClientKit;
import vip.yeee.memo.integrate.common.apiauth.client.kit.JwtClientKit;
import vip.yeee.memo.integrate.common.apiauth.client.model.ApiAuthedUser;
import vip.yeee.memo.integrate.base.util.JacksonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 14:38
 */
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final static Logger log = LogUtils.commonAuthLog();
    @Resource
    private JwtClientKit jwtClientKit;
    @Resource
    private JwsClientKit jwsClientKit;
    private static final String JWT_TOKEN_HEADER = "utoken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(JWT_TOKEN_HEADER);
        if (StrUtil.isNotBlank(token)) {
            Claims claims = jwtClientKit.getTokenClaim(token);
            if (claims != null) {
                ApiAuthedUser authedUser = JacksonUtils.toJavaBean(claims.getSubject(), ApiAuthedUser.class);
                ApiSecurityContext.curUser.set(authedUser);
            }
//            PayloadDto payloadDto = jwsClientKit.verifyTokenByRSA(token);
//            ApiAuthedUser authedUser = JacksonUtils.toJavaBean(payloadDto.getSub(), ApiAuthedUser.class);
//            ApiSecurityContext.curUser.set(authedUser);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ApiSecurityContext.curUser.remove();
    }
}
