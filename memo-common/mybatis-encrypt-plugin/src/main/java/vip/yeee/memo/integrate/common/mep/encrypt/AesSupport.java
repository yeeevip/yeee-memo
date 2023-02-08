package vip.yeee.memo.integrate.common.mep.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.integrate.common.mep.Encrypt;
import vip.yeee.memo.integrate.common.mep.type.SensitiveType;
import vip.yeee.memo.integrate.common.mep.type.SensitiveTypeHandler;
import vip.yeee.memo.integrate.common.mep.type.SensitiveTypeRegistry;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 数据脱敏用到的AES加解密类
 */
@Slf4j
public class AesSupport implements Encrypt {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String EN_PREFIX = "@:";

    private final String password;
    private final SecretKeySpec secretKeySpec;
    private final SensitiveTypeHandler sensitiveTypeHandler = SensitiveTypeRegistry.get(SensitiveType.DEFAULT);

    public AesSupport(String password) throws NoSuchAlgorithmException {

        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password should not be null!");
        }

        this.password = password;
        this.secretKeySpec = getSecretKey(password);
    }

    @Override
    public String encrypt(String value){
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] content = value.getBytes(StandardCharsets.UTF_8);
            byte[] encryptData = cipher.doFinal(content);

            return EN_PREFIX + new String(Base64.getEncoder().encode(encryptData), StandardCharsets.UTF_8);
        } catch (Exception e){
            log.error("AES加密时出现问题，密钥为：{}",sensitiveTypeHandler.handle(password), e);
            throw new IllegalStateException("AES加密时出现问题"+e.getMessage(),e);
        }
    }

    @Override
    public String decrypt(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        if (!StringUtils.startsWith(value, EN_PREFIX)) {
            return value;
        }
        value =  StringUtils.removeStart(value, EN_PREFIX);
        try {
            byte[] encryptData = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] content = cipher.doFinal(encryptData);
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密时出现问题，密钥为:{},密文为：{}",sensitiveTypeHandler.handle(password),value, e);
            throw new IllegalStateException("AES解密时出现问题"+e.getMessage(),e);
        }

    }

    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException{
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //AES 要求密钥长度为 128
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kg.init(128, random);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
}
