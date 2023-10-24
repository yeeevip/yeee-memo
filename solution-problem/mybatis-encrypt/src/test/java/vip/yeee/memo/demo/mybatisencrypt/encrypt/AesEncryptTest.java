package vip.yeee.memo.demo.mybatisencrypt.encrypt;

import org.junit.jupiter.api.Test;
import vip.yeee.memo.common.encrypt.mybatis.encrypt.AesSupport;

import java.security.NoSuchAlgorithmException;

public class AesEncryptTest {

    @Test
    public void test() throws NoSuchAlgorithmException {
        String key="1870577f29b17d6787782f35998c4a79";
        String src ="测试原文";
        AesSupport aesSupport = new AesSupport(key);
        String result = aesSupport.encrypt(src);
        System.out.println(result);
        String src2 = aesSupport.decrypt(result);
        System.out.println(src2);
    }
}
