package vip.yeee.memo.common.appauth.server.kit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import vip.yeee.memo.common.appauth.server.model.dto.PayloadDto;
import vip.yeee.memo.common.appauth.server.properties.ApiAuthServerProperties;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/30 10:21
 */
@ConditionalOnProperty(value = "yeee.apiauth.jwt.useJws", havingValue = "true")
@Component
public class JwsServerKit {

    @Resource
    @Qualifier("jwsKeyPair")
    private KeyPair keyPair;
    @Resource
    private ApiAuthServerProperties apiAuthServerProperties;

    /**
     * 使用HMAC算法生成Token
     * @param payloadStr
     * @param secret
     * @return
     */
    public String generateTokenByHMAC(String payloadStr, String secret) throws JOSEException {
        //创建JWS头，设置签名算法和类型
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT).build();
        //将负载信息封装到Payload中
        Payload payload = new Payload(payloadStr);
        //创建JWS对象
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        //创建HMAC签名器
        JWSSigner jwsSigner = new MACSigner(secret);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    public String generateTokenByRSA(PayloadDto payloadDto) throws JOSEException {
        //创建JWS头，设置签名算法和类型
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();
        //将负载信息封装到payload中
        Payload payload = new Payload(JSONObject.toJSONString(payloadDto));
        //创建JWS对象
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //创建RSA签名器
        JWSSigner jwsSigner = new RSASSASigner(keyPair.getPrivate(), true);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    public String generateTokenByRSA(String subject) throws JOSEException {
        Date now = new Date();
        Date exp = DateUtil.offsetSecond(now,apiAuthServerProperties.getExpire());
        PayloadDto payloadDto = new PayloadDto()
                .setSub(subject)
                .setIat(now.getTime())
                .setExp(exp.getTime())
                .setJti(IdUtil.simpleUUID());
        //创建JWS头，设置签名算法和类型
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();
        //将负载信息封装到payload中
        Payload payload = new Payload(JSONObject.toJSONString(payloadDto));
        //创建JWS对象
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //创建RSA签名器
        JWSSigner jwsSigner = new RSASSASigner(keyPair.getPrivate(), true);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    /**
     * 使用RSA算法生成Token
     * @param payloadStr
     * @param rsaKey
     * @return
     */
    public String generateTokenByRSA(String payloadStr, RSAKey rsaKey) throws JOSEException {
        //创建JWS头，设置签名算法和类型
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();
        //将负载信息封装到payload中
        Payload payload = new Payload(payloadStr);
        //创建JWS对象
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        //创建RSA签名器
        JWSSigner jwsSigner = new RSASSASigner(rsaKey,true);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    /**
     * 获取默认的RSAKey
     */
    public KeyPair getDefaultRSAKeyPair() {
        //从classpath下获取RSA密钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "vipyeee".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "vipyeee".toCharArray());
    }

}
