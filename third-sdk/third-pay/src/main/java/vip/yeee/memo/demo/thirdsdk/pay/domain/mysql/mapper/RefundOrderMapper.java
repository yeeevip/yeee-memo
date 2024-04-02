package vip.yeee.memo.demo.thirdsdk.pay.domain.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.yeee.memo.demo.thirdsdk.pay.domain.mysql.entity.RefundOrder;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/21 10:15
 */
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

    @Select("select * from t_refund_order where order_code = #{orderCode}")
    RefundOrder queryRefundOrderByCode(@Param("orderCode") String orderCode);
}
