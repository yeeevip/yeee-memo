package vip.yeee.memo.demo.scloud.tac.seatapgsql.domain.pgsql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import vip.yeee.memo.demo.scloud.tac.seatapgsql.typehandler.JTsGeometryTypeHandler;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/18 17:23
 */
@Data
@TableName("test1")
public class Test1 {

    private Long id;

    private String name;

    @TableField(typeHandler = JTsGeometryTypeHandler.class)
    private Geometry location;

}
