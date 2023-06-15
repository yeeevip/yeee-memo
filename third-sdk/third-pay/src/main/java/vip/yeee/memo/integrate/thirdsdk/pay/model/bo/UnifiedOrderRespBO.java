package vip.yeee.memo.integrate.thirdsdk.pay.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 16:50
 */
@Data
public class UnifiedOrderRespBO {

    /** 支付订单号 **/
    private String payOrderId;

    private String mchId;

    /** 商户订单号 **/
    private String mchOrderNo;

    /** 订单状态 **/
    private Integer orderState;

    /** 支付参数 **/
    private String payData;

    /** 渠道返回错误代码 **/
    private String errCode;

    /** 渠道返回错误信息 **/
    private String errMsg;

    /** 是否JSON序列化：根据情况选择 **/
    @JSONField(serialize = false)
    private ChannelRetMsgBO channelRetMsg;
}
