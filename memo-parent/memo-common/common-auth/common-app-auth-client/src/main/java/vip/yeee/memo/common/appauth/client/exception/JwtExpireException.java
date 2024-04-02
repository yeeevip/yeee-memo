package vip.yeee.memo.common.appauth.client.exception;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/30 11:15
 */
public class JwtExpireException extends RuntimeException {
    public JwtExpireException(String message) {
        super(message);
    }
}
