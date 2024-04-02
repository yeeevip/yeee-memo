package vip.yeee.memo.demo.thirdsdk.pay.model.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/3/16 11:55
 */
@Data
public class OrderRefundReqVO {

    @NotBlank
    private String orderCode;

}
