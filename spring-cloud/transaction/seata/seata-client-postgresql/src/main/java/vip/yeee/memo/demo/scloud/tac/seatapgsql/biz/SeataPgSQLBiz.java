package vip.yeee.memo.demo.scloud.tac.seatapgsql.biz;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.scloud.tac.seatapgsql.domain.pgsql.entity.Test1;
import vip.yeee.memo.demo.scloud.tac.seatapgsql.domain.pgsql.service.Test1Service;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/5/3 0:39
 */
@Slf4j
@Component
public class SeataPgSQLBiz {

    // 注意：这里需要有一个GeometryFactory实例来创建几何对象，通常可以通过工厂方法来获取
    private static final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    @Resource
    private Test1Service test1Service;

    public Void seataExecOpr() {
        Test1 test1 = new Test1();
        test1.setId(IdUtil.getSnowflake(1, 1).nextId());
        test1.setName(RandomUtil.randomString(6));
        Point point = geometryFactory.createPoint(new Coordinate(36.031, 120.173));
        test1.setLocation(point);
        test1Service.save(test1);
        log.info("seataExecOpr--->pgsql SUCCESS！！！");
        return null;
    }
}
