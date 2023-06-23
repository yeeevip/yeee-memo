package vip.yeee.memo.demo.design.pattern.behavior;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 策略模式
 *
 * @author yeeee
 * @since 2022/7/21 10:06
 */
public class StrategyPattern {
}

@Component
class HandlerContext {

    @Resource
    private Map<String, Handler> handlerMap;

    public Handler getHandler(StrategyEnum strategyEnum) throws Exception {
        if (CollectionUtil.isEmpty(handlerMap) || !handlerMap.containsKey(strategyEnum.getCode())) {
            throw new Exception("没有找到对应的handler");
        }
        return handlerMap.get(strategyEnum.getCode());
    }

}

@Slf4j
@Component
class Handler1 implements Handler {

    @Override
    public void handle() {
        log.info("Handler1");
    }
}

@Slf4j
@Component
class Handler2 implements Handler {

    @Override
    public void handle() {
        log.info("Handler2");
    }
}

@Slf4j
@Component
class Handler3 implements Handler {

    @Override
    public void handle() {
        log.info("Handler3");
    }
}

interface Handler {
    void handle();
}

@Getter
enum StrategyEnum {
    HANDLER1("handler1", "处理1"),
    HANDLER2("handler2", "处理2"),
    HANDLER3("handler3", "处理3"),
    ;
    private final String code;
    private final String desc;

    StrategyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
