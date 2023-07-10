package vip.yeee.memo.base.redis.constant;

public class RedisConstant {

    public static final String KEY_PREFIX = "YEEE:";

    /**
     * value前缀
     */
    public static final String KEY_PREFIX_VALUE = KEY_PREFIX + "VALUE:";

    /**
     * set前缀
     */
    public static final String KEY_PREFIX_SET = KEY_PREFIX + "SET:";

    /**
     * list前缀
     */
    public static final String KEY_PREFIX_LIST = KEY_PREFIX + "LIST:";

    /**
     * hash前缀
     */
    public static final String KEY_PREFIX_HASH = KEY_PREFIX + "HASH:";

    /**
     * zset前缀
     */
    public static final String KEY_PREFIX_ZSET= KEY_PREFIX + "ZSET:";

    /**
     * 分
     */
    public static final Long MINUTE_EXPIRE = 60L;

    /**
     * 小时
     */
    public static final Long HOUR_EXPIRE = 60L * 60;

    /**
     * 90 分钟
     */
    public static final Long NINETY_MINUTE_EXPIRE = 60L * 90;

    /**
     * 天
     */
    public static final Long DAY_EXPIRE = 60L * 60 * 24;
}
