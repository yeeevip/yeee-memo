package vip.yeee.memo.demo.springcloud.apiauth.server.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/28 16:34
 */
@Data
public class UserLoginRequest {

    @NotBlank(message = "用户名不能空")
    private String username;
    @NotBlank(message = "密码不能空")
    private String password;

}
