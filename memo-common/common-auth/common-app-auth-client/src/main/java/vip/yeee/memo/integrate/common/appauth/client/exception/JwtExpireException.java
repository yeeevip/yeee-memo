package vip.yeee.memo.integrate.common.appauth.client.exception;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/30 11:15
 */
public class JwtExpireException extends RuntimeException {
    public JwtExpireException(String message) {
        super(message);
    }
}
