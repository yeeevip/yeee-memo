package vip.yeee.memo.common.sso.thirdapp.model.dto;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/20 18:49
 */
@Data
public class ThirdAppDto {

    private String id;

    private String appKey;

    private String appSecret;

    //合作方名称
    private String appName;

    // 获取ticket用户信息url
    private String ticketUrl;

    //启用状态 0启用 1关闭
    private Integer status;

}
