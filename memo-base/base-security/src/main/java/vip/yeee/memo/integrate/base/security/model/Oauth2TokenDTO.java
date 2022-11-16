package vip.yeee.memo.integrate.base.security.model;

import lombok.Data;

/**
 * Oauth2获取Token返回信息封装
 */
@Data
public class Oauth2TokenDTO {

    private String token;

    private String refreshToken;

    private String tokenHead;

    private int expiresIn;
}
