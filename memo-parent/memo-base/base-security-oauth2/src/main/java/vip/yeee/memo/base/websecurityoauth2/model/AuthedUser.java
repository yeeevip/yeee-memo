package vip.yeee.memo.base.websecurityoauth2.model;

import lombok.Data;

import java.util.Set;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/25 14:22
 */
@Data
public class AuthedUser {

    /**
     * ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 权限
     */
    private Set<String> permissions;
    /**
     * 角色
     */
    private Set<String> roles;

    private Boolean superAdmin;

}
