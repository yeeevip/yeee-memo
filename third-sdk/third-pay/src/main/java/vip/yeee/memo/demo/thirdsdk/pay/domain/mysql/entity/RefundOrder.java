package vip.yeee.memo.demo.thirdsdk.pay.domain.mysql.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_refund_order")
public class RefundOrder {

    @TableId
    private Long id;

    private String lesseeId;

    private String code;

    private String outCode;

    private String mchId;

    /**
     * 支付渠道
     */
    private String channel;

    private String orderCode;

    /**
     * 退款金额,单位分
     */
    private BigDecimal amount;

    /**
     * 三位货币代码,人民币:cny
     */
    private String currency;

    /**
     * 退款状态:0-订单生成,10-退款中,20-退款成功,30-退款失败,40-退款任务关闭
     */
    private Integer state;

    /**
     * 订单支付成功时间
     */
    private LocalDateTime successTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer deleted;

}
