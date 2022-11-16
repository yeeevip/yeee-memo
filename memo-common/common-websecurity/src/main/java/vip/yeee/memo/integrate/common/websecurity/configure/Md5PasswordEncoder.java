package vip.yeee.memo.integrate.common.websecurity.configure;

import cn.hutool.crypto.SecureUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/17 14:00
 */
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return SecureUtil.md5().digestHex((String) rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return  SecureUtil.md5().digestHex((String) rawPassword).equals(encodedPassword);
    }
}
