package vip.yeee.memo.integrate.design.practice.statics.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import vip.yeee.memo.integrate.design.practice.statics.vo.StaticBo;
import vip.yeee.memo.integrate.design.practice.statics.vo.StaticException;

import java.util.Collections;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/3 11:48
 */
@Slf4j
public class StaticContext {

    public static StaticHandler getAdvertHandler() {
        return getHandler(Subject.AD_ADVERT);
    }

    public static StaticHandler getLaunchHandler() {
        return getHandler(Subject.AD_LAUNCH);
    }

    public static StaticHandler getSpaceHandler() {
        return getHandler(Subject.AD_SPACE);
    }

    public static StaticHandler getHandler(Subject subject) {
        try {
            return  (StaticHandler) SpringContextUtils.getBean(subject.getCode());
        } catch (BeansException e) {
            log.error("【静态化】-获取处理器失败，subject = {}", subject.getCode(), e);
            throw new StaticException("没有找到对应的处理器");
        }
    }

    public static void execDefaultHandle() {
        StaticBo staticBo = StaticBo.genBo(StaticBo.OprType.OPEN, "11", Collections.singletonList("999"));
        StaticContext.getHandler(Subject.AD_DEFAULT).handle(staticBo);
    }

    public static void execAdvertHandle(StaticBo.OprType oprType, List<String> advertIds) {
        try {
            StaticBo staticBo = StaticBo.genBo(oprType, "11", advertIds);
            StaticContext.getAdvertHandler().handle(staticBo);
        } catch (StaticException e) {
            StaticContext.execDefaultHandle();
        }
    }

    public static void execLaunchHandle(StaticBo.OprType oprType, List<String> launchIds) {
        try {
            StaticBo staticBo = StaticBo.genBo(oprType, "11", launchIds);
            StaticContext.getLaunchHandler().handle(staticBo);
        } catch (StaticException e) {
            StaticContext.execDefaultHandle();
        }
    }

    public static void execSpaceHandle(StaticBo.OprType oprType, List<String> spaceIds) {
        try {
            StaticBo staticBo = StaticBo.genBo(oprType, "11", spaceIds);
            StaticContext.getSpaceHandler().handle(staticBo);
        } catch (StaticException e) {
            StaticContext.execDefaultHandle();
        }
    }

    @Getter
    public enum Subject {

        AD_DEFAULT("adDefaultStaticHandler", "默认"),
        AD_ADVERT("adAdvertStaticHandler", "广告"),
        AD_LAUNCH("adLaunchStaticHandler", "投放"),
        AD_SPACE("adSpaceStaticHandler", "广告位"),
        ;

        private final String code;
        private final String desc;

        Subject(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

    public static class SpringContextUtils {

        public static Object getBean(String code) {
            return null;
        }
    }

}
