package vip.yeee.memo.demo.thirdsdk.pay.model.bo;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 16:50
 */
@Data
public class TransferReqBO {

    private String transferId;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 收款账号
     */
    private String accountNo;

    /**
     * 转账金额,单位分
     */
    private Long amount;

    /**
     * 转账备注信息
     */
    private String transferDesc;

    /**
     * 特定渠道发起额外参数
     */
    private String channelExtra;

    private String clientIp;

}
