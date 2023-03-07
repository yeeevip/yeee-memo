package vip.yeee.memo.integrate.springcloud.webresource.server1.model.request;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/17 13:22
 */
@Data
public class UserAuthRequest {

    private String username;
    private String password;

}
