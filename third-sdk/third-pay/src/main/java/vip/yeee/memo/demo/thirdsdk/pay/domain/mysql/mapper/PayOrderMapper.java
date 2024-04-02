package vip.yeee.memo.demo.thirdsdk.pay.domain.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import vip.yeee.memo.demo.thirdsdk.pay.domain.mysql.entity.PayOrder;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/21 10:15
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    @Update("update cloud_entry_pay_order set state = #{afterState}, update_time = now() where id = #{orderId} and state = #{preState}")
    int updateOrderRefundState(@Param("orderId") Long orderId, @Param("preState") Integer preState, @Param("afterState") Integer afterState);

}
