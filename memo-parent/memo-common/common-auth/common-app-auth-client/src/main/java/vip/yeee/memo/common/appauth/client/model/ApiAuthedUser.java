package vip.yeee.memo.common.appauth.client.model;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/28 15:34
 */
@Data
public class ApiAuthedUser {
    /**
     * ID
     */
    private String id;

    private String uid;

    private String openid;
    /**
     * 用户名
     */
    private String username;

    private String source;
}
