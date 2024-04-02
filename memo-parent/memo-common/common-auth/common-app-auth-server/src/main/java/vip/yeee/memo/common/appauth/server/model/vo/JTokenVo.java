package vip.yeee.memo.common.appauth.server.model.vo;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/28 17:06
 */
@Data
public class JTokenVo {
    
    private String accessToken;
    private Long expireIn;

}
