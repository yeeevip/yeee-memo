package vip.yeee.memo.common.appauth.server.kit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import vip.yeee.memo.common.appauth.server.model.vo.JTokenVo;
import vip.yeee.memo.common.appauth.server.properties.ApiAuthServerProperties;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/28 15:08
 */
@Component
public class JwtServerKit {

    @Resource
    private ApiAuthServerProperties apiAuthServerProperties;

    /**
     * 生成token
     */
    public JTokenVo createToken(String subject) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + apiAuthServerProperties.getExpire() * 1000); //过期时间
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, apiAuthServerProperties.getSecret())
                .compact();
        JTokenVo tokenVo = new JTokenVo();
        tokenVo.setAccessToken(accessToken);
        tokenVo.setExpireIn(expireDate.getTime());
        return tokenVo;
    }

    /**
     * 获取token中注册信息
     */
    public Claims getTokenClaim(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(apiAuthServerProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
//            e.printStackTrace();
            return null;
            /*  catch (ExpiredJwtException e){
                    return e.getClaims(); //防止jwt过期解析报错
                }
            */
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


}
