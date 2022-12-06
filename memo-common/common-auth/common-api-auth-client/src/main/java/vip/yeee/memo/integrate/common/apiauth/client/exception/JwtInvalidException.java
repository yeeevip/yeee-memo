package vip.yeee.memo.integrate.common.apiauth.client.exception;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/30 11:14
 */
public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message) {
        super(message);
    }
}
