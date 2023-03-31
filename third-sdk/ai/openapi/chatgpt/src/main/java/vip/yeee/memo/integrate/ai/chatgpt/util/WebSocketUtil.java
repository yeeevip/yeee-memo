package vip.yeee.memo.integrate.ai.chatgpt.util;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/1 2:17
 */
public class WebSocketUtil {

    public static InetSocketAddress getRemoteAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();

        //在Tomcat 8.0.x版本有效
        InetSocketAddress addr0 = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
        System.out.println("clientIP0" + addr0);
        //在Tomcat 8.5以上版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        System.out.println("clientIP1" + addr);
        return addr;
}


    private static Object getFieldInstance(Object obj, String fieldPath) {
        String fields[] = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }

        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
            }
        }

        return null;
    }

    public static String getIp(Session session) {
        return WebSocketUtil.getRemoteAddress(session).getAddress().toString().substring(1);
    }
}
