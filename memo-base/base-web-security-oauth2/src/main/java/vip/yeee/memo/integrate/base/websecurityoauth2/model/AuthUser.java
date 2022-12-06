package vip.yeee.memo.integrate.base.websecurityoauth2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    private String userId;
    private String username;
    private String password;
    private Integer state;
    private Set<String> permissions;
    private Set<String> roles;
}
