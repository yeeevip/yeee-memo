package vip.yeee.memo.demo.scloud.rpc.feign03.biz;

import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.scloud.rpc.feign03.feign.Feign01FeignClient;
import vip.yeee.memo.demo.scloud.rpc.feign03.feign.Feign02FeignClient;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/2/21 14:06
 */
@Component
public class Feign03Biz {

    @Resource
    private Feign01FeignClient feign01FeignClient;
    @Resource
    private Feign02FeignClient feign02FeignClient;

    public String getData() {
        return feign01FeignClient.getData().getData() + feign02FeignClient.getData().getData();
    }
}
