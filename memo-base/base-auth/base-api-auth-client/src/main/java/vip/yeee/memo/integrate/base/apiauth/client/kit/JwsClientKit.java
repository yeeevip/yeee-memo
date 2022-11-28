package vip.yeee.memo.integrate.base.apiauth.client.kit;

import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vip.yeee.memo.integrate.base.apiauth.client.exception.JwtExpireException;
import vip.yeee.memo.integrate.base.apiauth.client.exception.JwtInvalidException;
import vip.yeee.memo.integrate.base.apiauth.client.model.dto.PayloadDto;
import vip.yeee.memo.integrate.base.apiauth.client.properties.ApiAuthClientProperties;
import vip.yeee.memo.integrate.common.base.utils.JacksonUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/30 10:21
 */
@Component
public class JwsClientKit {

    @Resource
    private ApiAuthClientProperties authClientProperties;
    @Resource
    @Qualifier("lbRestTemplate")
    private RestTemplate restTemplate;

    /**
     * 使用HMAC验证Token
     * @param token
     * @param secret
     * @return
     */
    public PayloadDto verifyTokenByHMAC(String token, String secret) throws Exception {
        //从token中解析JWS对象
        JWSObject jwsObject = JWSObject.parse(token);
        //创建HMAC验证器
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        if (!jwsObject.verify(jwsVerifier)){
            throw new JwtInvalidException("token签名不合法");
        }
        String payload = jwsObject.getPayload().toString();
        System.out.println(payload);
        PayloadDto payloadDto = JacksonUtils.toJavaBean(payload, PayloadDto.class);
        if (payloadDto.getExp() < new Date().getTime()){
            throw new JwtExpireException("token已过期！");
        }
        return payloadDto;
    }

    public PayloadDto verifyTokenByRSA(String token) throws ParseException, JOSEException, IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(authClientProperties.getSecretUrl(), String.class);
        return this.verifyTokenByRSA(token, (RSAKey) JWKSet.parse(response.getBody()).getKeys().get(0));
    }

    /**
     * 使用RSA算法验证token
     * @param token
     * @param rsaKey
     * @return
     */
    public PayloadDto verifyTokenByRSA(String token, RSAKey rsaKey) throws ParseException, JOSEException {
        //从token中解析JWS对象
        JWSObject jwsObject = JWSObject.parse(token);
        RSAKey publicRsaKey = rsaKey.toPublicJWK();
        //使用RSA公钥创建RSA验证器
        JWSVerifier jwsVerifier = new RSASSAVerifier(publicRsaKey);
        if (!jwsObject.verify(jwsVerifier)){
            throw new JwtInvalidException("token签名不合法！");
        }
        String payload = jwsObject.getPayload().toString();
        PayloadDto payloadDto = JSONUtil.toBean(payload, PayloadDto.class);
        if (payloadDto.getExp() < new Date().getTime()){
            throw new JwtExpireException("token已过期！");
        }
        return payloadDto;
    }

}
