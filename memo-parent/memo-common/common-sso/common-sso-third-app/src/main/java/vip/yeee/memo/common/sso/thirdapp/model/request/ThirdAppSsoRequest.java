package vip.yeee.memo.common.sso.thirdapp.model.request;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/20 18:49
 */
@Data
public class ThirdAppSsoRequest {

    // 三方应用
    private String appKey;
    // 三方当前登录用户标识
    private String ticket;
    private String timestamp;
    private String authFailUrl;
    private String authSuccessUrl;
    private String signature;

}
