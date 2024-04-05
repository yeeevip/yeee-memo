package vip.yeee.memo.common.appauth.client.constant;

import lombok.experimental.UtilityClass;

/**
 * 权限相关常量定义
 */
@UtilityClass
public class ApiAuthConstant {

    /**
     * 认证信息Http请求头
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";

    /**
     * JWT令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * token参数
     */
    public static final String TOKEN = "Utoken";

    public final static String[] BASE_EXCLUDE_PATTERNS = new String[] {
            "/"
            , "/doc.html"
            , "/swagger-resources/**"
            , "/swagger/**"
            , "/**/v2/api-docs"
            , "/**/*.js"
            , "/**/*.css"
            , "/**/*.png"
            , "/**/*.jpg"
            , "/**/*.ico"
            , "/**/*.html",
            "/webjars/springfox-swagger-ui/**"
            , "/actuator/**"
            , "/error"
            , "/inner/**"
            , "/upload/**"
    };

}
