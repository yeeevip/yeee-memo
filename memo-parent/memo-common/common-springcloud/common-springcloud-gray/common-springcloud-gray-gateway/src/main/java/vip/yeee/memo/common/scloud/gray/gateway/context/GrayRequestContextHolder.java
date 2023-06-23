package vip.yeee.memo.common.scloud.gray.gateway.context;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/6 16:28
 */
public class GrayRequestContextHolder {

    private final static ThreadLocal<String> apiVersion = new ThreadLocal<>();

    public static void setApiVersion(String version) {
        apiVersion.set(version);
    }

    public static String getApiVersion() {
        return apiVersion.get();
    }

    public static void remove() {
        apiVersion.remove();
    }
}
