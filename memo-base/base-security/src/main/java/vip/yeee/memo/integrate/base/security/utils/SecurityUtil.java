package vip.yeee.memo.integrate.base.security.utils;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.yeee.memo.integrate.base.security.model.SecurityUser;
import vip.yeee.memo.integrate.base.security.constant.AuthConstant;
import vip.yeee.memo.integrate.base.web.utils.SpringContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class SecurityUtil {

    /**
     * 返回数据封装
     */
    public static void write(HttpServletResponse response, Object data) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSONObject.toJSONString(data, SerializerFeature.WriteNullStringAsEmpty));
        IoUtil.close(out);
    }

    /**
     * 返回当前用户ID
     */
    public static Integer currentUserId(){
        try {
            return currentSecurityUser().getId();
        } catch (Exception e) {
            //log.warn("currentUserId error");
        }
        return null;
    }

    public static SecurityUser currentSecurityUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        String t = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        return (SecurityUser) ((TokenStore) SpringContextUtils.getBean(TokenStore.class)).readAuthentication(t).getPrincipal();
    }

    public static OAuth2AccessToken getOAuth2AccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        String t = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        return ((TokenStore) SpringContextUtils.getBean(TokenStore.class)).readAccessToken(t);
    }

}
