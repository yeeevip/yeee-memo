package vip.yeee.memo.integrate.design.practice.statics.ad;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.design.practice.statics.vo.StaticAdVo;
import vip.yeee.memo.integrate.design.practice.statics.vo.StaticBo;
import vip.yeee.memo.integrate.design.practice.statics.vo.TCmsSite;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * 默认处理器，全量刷新
 *
 * @author yeeee
 * @since 2023/1/9 15:26
 */
@Component
public class AdDefaultStaticHandler extends AbstractAdStaticHandler {

    @Override
    protected List<StaticBo.OprType> supportOprTypes() {
        return Arrays.asList(StaticBo.OprType.OPEN, StaticBo.OprType.CLOSE
                , StaticBo.OprType.DELETE, StaticBo.OprType.UPDATE);
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleOpen(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) throws Exception {
        return this.getSiteAllStaticAdvertVo(site);
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleClose(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) throws Exception {
        return this.getSiteAllStaticAdvertVo(site);
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleDelete(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) throws Exception {
        return this.getSiteAllStaticAdvertVo(site);
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleUpdate(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) throws Exception {
        return this.getSiteAllStaticAdvertVo(site);
    }

    private TreeMap<String, StaticAdVo> getSiteAllStaticAdvertVo(TCmsSite site) throws Exception {
        return Maps.newTreeMap();
    }
}
