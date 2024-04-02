package vip.yeee.memo.demo.design.practice.statics.ad;

import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.design.practice.statics.vo.TCmsSite;
import vip.yeee.memo.demo.design.practice.statics.vo.StaticAdVo;
import vip.yeee.memo.demo.design.practice.statics.vo.StaticBo;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * 广告位操作
 *
 * @author https://www.yeee.vip
 * @since 2023/1/3 16:58
 */
@Component
public class AdSpaceStaticHandler extends AbstractAdStaticHandler {

    @Override
    protected List<StaticBo.OprType> supportOprTypes() {
        return Arrays.asList(StaticBo.OprType.CLOSE, StaticBo.OprType.UPDATE);
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleClose(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) {
        return adDataMap;
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleUpdate(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) throws Exception {
        return adDataMap;
    }
}
