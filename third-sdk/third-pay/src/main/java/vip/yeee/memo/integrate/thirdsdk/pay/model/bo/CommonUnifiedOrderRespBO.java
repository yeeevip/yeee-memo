package vip.yeee.memo.integrate.thirdsdk.pay.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 13:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonUnifiedOrderRespBO extends UnifiedOrderRespBO {

    /** 跳转地址 **/
    private String payUrl;

    /** 二维码地址 **/
    private String codeUrl;

    /** 二维码图片地址 **/
    private String codeImgUrl;

    /** 表单内容 **/
    private String formContent;
}
