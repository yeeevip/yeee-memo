//package vip.yeee.memo.integrate.base.webauth.server.configure;
//
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * description......
// * @author yeeee
// */
//public class JwtTokenEnhancer implements TokenEnhancer {
//
//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//        Map<String, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("enhance", "enhance info");
//        objectObjectHashMap.put("ceshi", "张三");
//        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(objectObjectHashMap);
//        return oAuth2AccessToken;
//    }
//}
