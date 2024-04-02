package vip.yeee.memo.demo.springcloud.apiauth.server.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.common.appauth.server.model.vo.JTokenVo;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.springcloud.apiauth.server.model.request.UserLoginRequest;
import vip.yeee.memo.demo.springcloud.apiauth.server.biz.ApiAuthBiz;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/28 16:02
 */
@RestController
public class ApiAuthController {

    @Resource
    private ApiAuthBiz apiAuthBiz;
    @Resource
    @Qualifier("jwsKeyPair")
    private KeyPair keyPair;

    @PostMapping("user/login")
    public CommonResult<JTokenVo> userLogin(@RequestBody UserLoginRequest request) throws Exception {
        return CommonResult.success(apiAuthBiz.userLogin(request));
    }

    @PostMapping("access/api")
    public CommonResult<String> accessApi() {
        return CommonResult.success(apiAuthBiz.accessApi());
    }

    @GetMapping("jws/token")
    public CommonResult<String> getJwsToken() {
        return CommonResult.success(apiAuthBiz.getJwsToken());
    }

    @GetMapping("jws/verify")
    public CommonResult<Void> verifyJwsToken(String token) {
        return CommonResult.success(apiAuthBiz.verifyJwsToken(token));
    }

    @GetMapping("/rsa/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
