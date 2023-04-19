package vip.yeee.memo.integrate.base.websecurityoauth2.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/28 16:02
 */
@Data
public class SysRole implements GrantedAuthority {

    private Integer id;
    private String code;
    private String name;

    @Override
    public String getAuthority() {
        return code;
    }
}

