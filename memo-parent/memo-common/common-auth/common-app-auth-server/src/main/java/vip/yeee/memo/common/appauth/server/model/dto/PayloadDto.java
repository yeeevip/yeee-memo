package vip.yeee.memo.common.appauth.server.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/30 10:14
 */
@Data
@Accessors(chain = true)
public class PayloadDto {
    // 主题
    private String sub;
    // 签发时间
    private Long iat;
    // 过期时间
    private Long exp;
    // JWT ID
    private String jti;
    // 用户名
    private String username;
    // 用户权限
    private List<String> authorities;
}
