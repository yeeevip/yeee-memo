package vip.yeee.memo.integrate.base.websecurity.model;

import lombok.Data;

/**
 * Oauth2获取Token返回信息封装
 */
@Data
public class Oauth2TokenVo {

    private String token;

    private String refreshToken;

    private String tokenHead;

    private int expiresIn;
}
