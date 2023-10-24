package vip.yeee.memo.common.encrypt.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import vip.yeee.memo.common.encrypt.mybatis.annotation.SensitiveEncryptEnabled;
import vip.yeee.memo.common.encrypt.mybatis.utils.PluginUtils;
import vip.yeee.memo.common.encrypt.mybatis.Encrypt;
import vip.yeee.memo.common.encrypt.mybatis.annotation.EncryptField;
import vip.yeee.memo.common.encrypt.mybatis.annotation.SensitiveBind;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveType;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveTypeRegistry;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 对响应结果进行拦截处理,对需要解密的字段进行解密
 * SQL样例：
 *  1. UPDATE tbl SET x=?, y =
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {java.sql.Statement.class})
})
@Slf4j
public class DecryptReadInterceptor implements Interceptor {

    private static final String MAPPED_STATEMENT="mappedStatement";

    private final Encrypt encrypt;
    public DecryptReadInterceptor(Encrypt encrypt) {
        Objects.requireNonNull(encrypt,"encrypt should not be null!");
        this.encrypt = encrypt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final List<Object> results = (List<Object>)invocation.proceed();

        if (results.isEmpty()) {
            return results;
        }

        final ResultSetHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        final MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        final MappedStatement mappedStatement = (MappedStatement)metaObject.getValue(MAPPED_STATEMENT);
        final ResultMap resultMap = mappedStatement.getResultMaps().isEmpty() ? null : mappedStatement.getResultMaps().get(0);

        Object result0 = results.get(0);
        SensitiveEncryptEnabled sensitiveEncryptEnabled = result0.getClass().getAnnotation(SensitiveEncryptEnabled.class);
        if (sensitiveEncryptEnabled == null || !sensitiveEncryptEnabled.value()) {
            return results;
        }

        final Map<String, EncryptField> sensitiveFieldMap = getSensitiveByResultMap(resultMap);
        final Map<String, SensitiveBind> sensitiveBindMap = getSensitiveBindByResultMap(resultMap);

        if (sensitiveBindMap.isEmpty() && sensitiveFieldMap.isEmpty()) {
            return results;
        }

        for (Object obj: results) {
            final MetaObject objMetaObject = mappedStatement.getConfiguration().newMetaObject(obj);
            for (Map.Entry<String, EncryptField> entry : sensitiveFieldMap.entrySet()) {
                String property = entry.getKey();
                String value = (String) objMetaObject.getValue(property);
                if (value != null) {
                    String decryptValue = encrypt.decrypt(value);
                    objMetaObject.setValue(property, decryptValue);
                }
            }
            for (Map.Entry<String, SensitiveBind> entry : sensitiveBindMap.entrySet()) {

                String property = entry.getKey();

                SensitiveBind sensitiveBind = entry.getValue();
                String bindProperty = sensitiveBind.bindField();
                SensitiveType sensitiveType = sensitiveBind.value();
                try {
                    String value = (String) objMetaObject.getValue(bindProperty);
                    String resultValue =  SensitiveTypeRegistry.get(sensitiveType).handle(value);
                    objMetaObject.setValue(property,resultValue);
                } catch (Exception e){
                    //ignore it;
                }
            }
        }

        return results;
    }

    private Map<String,SensitiveBind> getSensitiveBindByResultMap(ResultMap resultMap) {
        if (resultMap == null) {
            return new HashMap<>(16);
        }
        Map<String, SensitiveBind> sensitiveBindMap = new HashMap<>(16);
        Class<?> clazz = resultMap.getType();
        List<Field> fields = PluginUtils.getFields(clazz);
        for (Field field : fields) {
            SensitiveBind sensitiveBind = field.getAnnotation(SensitiveBind.class);
            if (sensitiveBind != null) {
                sensitiveBindMap.put(field.getName(), sensitiveBind);
            }
        }
        return sensitiveBindMap;
    }

    private Map<String, EncryptField> getSensitiveByResultMap(ResultMap resultMap) {
        if (resultMap == null) {
            return new HashMap<>(16);
        }

        return getSensitiveByType(resultMap.getType());
    }

    private Map<String, EncryptField> getSensitiveByType(Class<?> clazz) {
        Map<String, EncryptField> sensitiveFieldMap = new HashMap<>(16);
        List<Field> fields = PluginUtils.getFields(clazz);
        for (Field field : fields) {
            EncryptField sensitiveField = field.getAnnotation(EncryptField.class);
            if (sensitiveField != null) {
                sensitiveFieldMap.put(field.getName(), sensitiveField);
            }
        }
        return sensitiveFieldMap;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // ignore
    }
}
