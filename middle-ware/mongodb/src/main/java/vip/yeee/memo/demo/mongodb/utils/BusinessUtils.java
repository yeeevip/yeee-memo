package vip.yeee.memo.demo.mongodb.utils;

import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/10/16 15:30
 */
public class BusinessUtils {

    public static Update buildMongoUpdate(Object o) {
        Update update = new Update();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if ("serialVersionUID".equals(field.getName())
                        || "id".equals(field.getName())) {
                    continue;
                }
                if (field.get(o) != null) {
                    update.set(field.getName(), field.get(o));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return update;
    }

}
