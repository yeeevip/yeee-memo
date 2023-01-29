package vip.yeee.memo.integrate.design.practice.statics.ad;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.design.practice.statics.base.AbstractStaticHandler;
import vip.yeee.memo.integrate.design.practice.statics.vo.StaticAdVo;
import vip.yeee.memo.integrate.design.practice.statics.vo.StaticDataVo;
import vip.yeee.memo.integrate.design.practice.statics.vo.TCmsSite;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/29 10:33
 */
@Slf4j
@Component
public abstract class AbstractAdStaticHandler extends AbstractStaticHandler<List<StaticAdVo>, TreeMap<String, StaticAdVo>> {

    @Override
    protected TreeMap<String, StaticAdVo> dataMapping(List<StaticAdVo> ts) {
        TreeMap<String, StaticAdVo> adDataMap = Optional
                .ofNullable(ts)
                .orElseGet(Lists::newArrayList)
                .stream()
                .collect(Collectors.toMap(StaticAdVo::getSpaceCode, Function.identity(), (o, n) -> n, TreeMap::new));
        return adDataMap;
    }

    @Override
    protected List<StaticAdVo> dataCollect(TreeMap<String, StaticAdVo> rs) {
        return Lists.newArrayList(rs.values());
    }

    @Override
    protected String getStaticPath(TCmsSite site) {
        return null;
    }

    @Override
    protected void postProcess(TCmsSite site, StaticDataVo<List<StaticAdVo>> staticDataVo, TreeMap<String, StaticAdVo> afterAdData) {

    }
}
