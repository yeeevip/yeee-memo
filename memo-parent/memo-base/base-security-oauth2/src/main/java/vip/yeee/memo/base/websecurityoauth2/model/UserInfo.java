package vip.yeee.memo.base.websecurityoauth2.model;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/10/12 17:29
 */
@Data
public class UserInfo {

    /**
     * ID
     */
    private String id;
    private String userType;
    /**
     * 用户名
     */
    private String username;
}
