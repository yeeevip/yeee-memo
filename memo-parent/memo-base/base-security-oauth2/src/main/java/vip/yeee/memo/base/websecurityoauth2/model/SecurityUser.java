package vip.yeee.memo.base.websecurityoauth2.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 登录用户信息
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * ID
     */
    private String id;

    private String userType;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
    /**
     * 登录客户端ID
     */
    private String clientId;
    /**
     * 权限数据
     */
    private List<SimpleGrantedAuthority> authorities;

    private Boolean superAdmin;

    public SecurityUser() {

    }

//    public SecurityUser(UserDto userDto) {
//        this.setId(userDto.getId());
//        this.setUsername(userDto.getUsername());
//        this.setPassword(userDto.getPassword());
//        this.setEnabled(userDto.getStatus() == 1);
//        this.setClientId(userDto.getClientId());
//        if (userDto.getRoles() != null) {
//            authorities = new ArrayList<>();
//            userDto.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
//        }
//    }

    public SecurityUser(String id, String userType, String username, String password, Integer state, Set<String> roles) {
        this.setId(id);
        this.setUserType(userType);
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(state == 0);
        if (roles != null) {
            authorities = new ArrayList<>();
            roles.forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
