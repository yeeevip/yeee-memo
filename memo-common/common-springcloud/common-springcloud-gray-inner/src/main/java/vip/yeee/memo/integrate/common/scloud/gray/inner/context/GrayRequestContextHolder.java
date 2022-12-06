package vip.yeee.memo.integrate.common.scloud.gray.inner.context;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/6 16:28
 */
public class GrayRequestContextHolder {

    private final static ThreadLocal<Boolean> grayTag = new ThreadLocal<>();

    public static void setGrayTag(Boolean tag) {
        grayTag.set(tag);
    }

    public static Boolean getGrayTag() {
        return grayTag.get();
    }

    public static void remove() {
        grayTag.remove();
    }
}
