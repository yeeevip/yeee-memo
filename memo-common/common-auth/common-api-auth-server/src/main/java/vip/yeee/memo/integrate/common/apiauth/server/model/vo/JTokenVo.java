package vip.yeee.memo.integrate.common.apiauth.server.model.vo;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/28 17:06
 */
@Data
public class JTokenVo {
    
    private String accessToken;
    private Long expireIn;

}
