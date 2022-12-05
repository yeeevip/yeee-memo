package vip.yeee.memo.integrate.base.apiauth.client.kit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.apiauth.client.properties.ApiAuthClientProperties;
import vip.yeee.memo.integrate.base.model.exception.BizException;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/28 15:08
 */
@Slf4j
@Component
public class JwtClientKit {

    @Resource
    private ApiAuthClientProperties apiAuthClientProperties;


    /**
     * 获取token中注册信息
     */
    public Claims getTokenClaim(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(apiAuthClientProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BizException("token过期");
        } catch (Exception e) {
            log.error("jwt 解析出错, token = {}", token, e);
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 验证token是否过期失效
     */
    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }

    /**
     * 获取token失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }

    /**
     * 获取用户从token中
     */
    public String getUserFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }

}
