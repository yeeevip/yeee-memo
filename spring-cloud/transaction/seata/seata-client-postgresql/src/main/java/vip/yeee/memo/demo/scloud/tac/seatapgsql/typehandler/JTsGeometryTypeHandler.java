package vip.yeee.memo.demo.scloud.tac.seatapgsql.typehandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.postgis.PGgeometry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * description......
 * mybatis 几何字段类型转换处理器 - geometry抽象类，由具体几何类型继承
 *
 * @author yeeee
 * @since 2024/5/10 10:13
 */
@MappedJdbcTypes({JdbcType.OTHER})
@Slf4j
public class JTsGeometryTypeHandler <T extends Geometry> extends BaseTypeHandler<T> {

    /**
     * SRS-EPSG
     */
    private static final int EPSG_CODE = 4490;
    private final WKTReader wktReader = new WKTReader();

    /**
     * 插入 - 插入时设置参数类型
     *
     * @param preparedStatement
     * @param i
     * @param parameter         GeoTools组织的几何类型实体
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T parameter, JdbcType jdbcType) throws SQLException {
        // 获取要插入的自定义类型的（GeoTools的Geometry实体）实体，并进行类型检查
        boolean typeCheck = parameter instanceof Geometry;
        if (!typeCheck) {
            preparedStatement.setObject(i, null);
            log.error("MyBatis类型转换器插入参数非GeoTools的Geometry对象");
        }

        // 通过几何的WKT格式字符串构建PG的Geometry
        PGgeometry pGgeometry = new PGgeometry(parameter.toText());
        org.postgis.Geometry geometry = pGgeometry.getGeometry();
        geometry.setSrid(EPSG_CODE);
        preparedStatement.setObject(i, pGgeometry);
    }

    /**
     * 获取 - 获取时转换回的自定义类型 - 根据列名获取
     *
     * @param resultSet
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        // 获取读取到的指定列存储的值并进行类型检查
        Object result = resultSet.getObject(columnName);
        boolean typeCheck = result instanceof PGgeometry;
        if (!typeCheck) {
            log.error("MyBatis类型转换器获取参数转自定义类型错误，列实际存储类型非org.postgis.PGgeometry，列实际存储类型");
            return null;
        }

        // 检查并返回目标转换类型实例
        return getResult((PGgeometry) result);
    }

    /**
     * 获取 - 获取时转换回的自定义类型 - 根据索引位置获取
     *
     * @param resultSet
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public T getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        // 获取读取到的指定列存储的值并进行类型检查
        Object result = resultSet.getObject(columnIndex);
        boolean typeCheck = result instanceof PGgeometry;
        if (!typeCheck) {
            log.error("MyBatis类型转换器获取参数转自定义类型错误，列实际存储类型非org.postgis.PGgeometry，列实际存储类型");
            return null;
        }

        // 检查并返回目标转换类型实例
        return getResult((PGgeometry) result);
    }

    /**
     * 获取 - 获取时转换回的自定义类型 - 根据存储过程获取
     *
     * @param callableStatement
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public T getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        // 获取读取到的指定列存储的值并进行类型检查
        Object result = callableStatement.getObject(columnIndex);
        boolean typeCheck = result instanceof PGgeometry;
        if (!typeCheck) {
            log.error("MyBatis类型转换器获取参数转自定义类型错误，列实际存储类型非org.postgis.PGgeometry，列实际存储类型");
            return null;
        }

        // 检查并返回目标转换类型实例
        return getResult((PGgeometry) result);
    }

    /**
     * pgGeometry实例转GeoTools的Geometry实例
     *
     * @param pgGeometry
     * @return
     */
    private T getResult(PGgeometry pgGeometry) {
        if (pgGeometry == null) {
            return null;
        }
        // 替换掉pgWKT中关于SRID的字符串部分
        // WKT-PG："SRID=4326;POINT(118.88888888 36.6666666666)"
        // WKT："POINT(118.88888888 36.6666666666)"
        String pgWkt = pgGeometry.toString();
        String wkt = pgWkt.replace(String.format("SRID=%s;", EPSG_CODE), "");

        // 通过WKT构建GeoTools的Geometry对象
        try {
            return (T) wktReader.read(wkt);
        } catch (Exception e) {
            log.error("解析WKT为GeoTools的Geometry对象失败,异常信息：" + e.toString());
            return null;
        }
    }


    // 从WKT中获取几何
    private Geometry getGeometryFromWkt(String wkt) {
        Geometry geometry = null;
        try {
            geometry = wktReader.read(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (geometry == null) {
            return null;
        }
        return geometry;
    }


    // 从WKT中获取几何
    private Geometry covertPgGeometry2JtsGeometry(PGgeometry pGgeometry) {
        StringBuffer wkt = new StringBuffer();
        pGgeometry.getGeometry().outerWKT(wkt);
        Geometry geometry = null;
        if (pGgeometry == null) {
            return null;
        } else {
            try {
                geometry = wktReader.read(wkt.toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return geometry;
        }
    }

    // 从WKT中获取几何
    private PGgeometry covertJTsGeometry2PgGeometry(Geometry geometry) throws SQLException {
        // 通过几何的WKT格式字符串构建PG的Geometry
        PGgeometry pGgeometry = new PGgeometry(geometry.toText());
        org.postgis.Geometry pgGeometry = pGgeometry.getGeometry();
        pgGeometry.setSrid(EPSG_CODE);
        return pGgeometry;
    }
}
