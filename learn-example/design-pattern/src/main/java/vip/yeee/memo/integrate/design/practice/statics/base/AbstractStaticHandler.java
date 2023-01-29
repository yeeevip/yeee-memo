package vip.yeee.memo.integrate.design.practice.statics.base;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.design.practice.statics.temp.TempService;
import vip.yeee.memo.integrate.design.practice.statics.vo.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/3 13:21
 */
@Slf4j
@Component
public abstract class AbstractStaticHandler<T, RS> implements StaticHandler {

    @Resource
    private TempService.OssKit ossKit;
    @Resource
    private TempService.ConfigApiFeignAPI configApiFeignAPI;
    @Resource
    private TempService.RedissonClient redissonClient;

    @Override
    public void handle(StaticBo bo) {
        if (bo.getSiteId() == null || CollectionUtil.isEmpty(bo.getSubjectIds())) {
            return;
        }
        if (!supportOprTypes().contains(bo.getOprType())) {
            log.info("【静态化】- 不支持的操作类型");
            return;
        }
        TCmsSite site = configApiFeignAPI.getSiteById(bo.getSiteId());
        if (site == null) {
            log.info("【静态化】- SITE不存在");
            return;
        }
        log.info("【静态化】- 开始, site = {}", site.getId());
        String staticPath = this.getStaticPath(site);
        Stopwatch stopwatch = Stopwatch.createStarted();
        RLock lock = redissonClient.getLock("ADVERT:STATIC:LOCK:" + bo.getSiteId());
        lock.lock();
        try {
            StaticDataVo<T> staticDataVo = ossKit.getStaticJson2Object(staticPath, new TypeReference<StaticDataVo<T>>() {});
            if (staticDataVo == null) {
                staticDataVo = new StaticDataVo<>();
                staticDataVo.setTimeStamp(String.valueOf(System.currentTimeMillis()));
                staticDataVo.setSiteName(site.getName());
            }
            RS rs = this.dataMapping(staticDataVo.getData());
            String beforeCont= JSONObject.toJSONString(rs);
            RS afterData = null;
            if (StaticBo.OprType.OPEN.equals(bo.getOprType())) {
                afterData = handleOpen(site, bo, rs);
            } else if (StaticBo.OprType.CLOSE.equals(bo.getOprType())) {
                afterData = handleClose(site, bo, rs);
            } else if (StaticBo.OprType.DELETE.equals(bo.getOprType())) {
                afterData = handleDelete(site, bo, rs);
            } else if (StaticBo.OprType.UPDATE.equals(bo.getOprType())) {
                afterData = handleUpdate(site, bo, rs);
            }
            if (JSONObject.toJSONString(afterData).equals(beforeCont)) {
                log.info("【静态化】- 内容未发生变化");
                return;
            }
            staticDataVo.setData(this.dataCollect(rs));
            ossKit.uploadObject2StaticJson(staticPath, staticDataVo);
            this.postProcess(site, staticDataVo, rs);
        } catch (Exception e) {
            log.info("【静态化】- 失败, site = {}", site.getId(), e);
            throw new StaticException("静态化失败");
        } finally {
            lock.unlock();
        }
        log.info("【静态化】- 结束，耗时：{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    protected abstract List<StaticBo.OprType> supportOprTypes();

    protected abstract RS dataMapping(T t);

    protected abstract T dataCollect(RS rs);

    protected abstract String getStaticPath(TCmsSite site);

    protected abstract void postProcess(TCmsSite site, StaticDataVo<T> staticDataVo, RS rs);

    protected RS handleOpen(TCmsSite site, StaticBo bo, RS rs) throws Exception {
        return rs;
    }

    protected RS handleClose(TCmsSite site, StaticBo bo, RS rs) throws Exception {
        return rs;
    }

    protected RS handleDelete(TCmsSite site, StaticBo bo, RS rs) throws Exception {
        return rs;
    }

    protected RS handleUpdate(TCmsSite site, StaticBo bo, RS rs) throws Exception {
        return rs;
    }

}
