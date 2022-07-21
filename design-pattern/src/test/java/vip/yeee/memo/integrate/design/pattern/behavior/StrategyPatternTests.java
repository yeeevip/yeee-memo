package vip.yeee.memo.integrate.design.pattern.behavior;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/21 10:29
 */
@SpringBootTest
public class StrategyPatternTests {

    @Resource
    private HandlerContext handlerContext;

    private final static Map<Integer, StrategyEnum> typeStrategyCodeMap = new HashMap<>(3);

    static {
        typeStrategyCodeMap.put(10, StrategyEnum.HANDLER1);
        typeStrategyCodeMap.put(20, StrategyEnum.HANDLER2);
        typeStrategyCodeMap.put(30, StrategyEnum.HANDLER3);
    }

    @Test
    public void test() throws Exception {
        Integer type = 20;
        handlerContext.getHandler(typeStrategyCodeMap.get(type))
                .handle();
    }

}
