package vip.yeee.memo.integrate.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/30 9:43
 */
@SpringBootTest
public class JasyptTest {

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    public void dencrypt() {
        String de = stringEncryptor.decrypt("TTLBz7OXBoqCaRdHWhN6GrAZGVowcvpbZLcwy/EbNRQ=");
        System.out.println(de);
    }

}
