package vip.yeee.memo.integrate.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonUtils {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JacksonUtils() {
    }

    public static String toJsonString(Object value) throws Exception {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Error to convert java object to string: {}", e.getMessage(), e);
            throw new Exception("无效的数据格式");
        }
    }

    /**
     * json字符串转换为复杂类型
     */
    public static <T> T toJavaBean(String jsonString, TypeReference<T> typeReference) throws Exception {
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error to convert json string [ {} ] to java object: {}", jsonString, e.getMessage(), e);
            throw new Exception("无效的数据格式");
        }
    }

    /**
     * json字符串转换为简单的Java对象
     */
    public static <T> T toJavaBean(String jsonString, Class<T> clazz) throws Exception {
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error to convert json string [ {} ] to java object: {}", jsonString, e.getMessage(), e);
            throw new Exception("无效的数据格式");
        }
    }

}
