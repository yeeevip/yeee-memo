package vip.yeee.memo.base.websecurityoauth2.constant;

import lombok.experimental.UtilityClass;

/**
 * 权限相关常量定义
 */
@UtilityClass
public class AuthConstant {

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
    public static final String TOKEN = "token";

    /**
     * client_id
     */
    public static final String AUTH_CLIENT_ID = "client_id";

    /**
     * client_secret
     */
    public static final String AUTH_CLIENT_SECRET = "client_secret";

    /**
     * grant_type
     */
    public static final String AUTH_GRANT_TYPE = "grant_type";

    /**
     * username
     */
    public static final String AUTH_USERNAME = "username";

    /**
     * userType
     */
    public static final String AUTH_USER_TYPE = "userType";

    /**
     * password
     */
    public static final String AUTH_PASSWORD = "password";

    public static final String ROLE_PREFIX = "ROLE_";

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
    };

}
