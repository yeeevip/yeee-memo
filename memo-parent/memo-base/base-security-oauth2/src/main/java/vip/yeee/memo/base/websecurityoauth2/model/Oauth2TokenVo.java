package vip.yeee.memo.base.websecurityoauth2.model;

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
