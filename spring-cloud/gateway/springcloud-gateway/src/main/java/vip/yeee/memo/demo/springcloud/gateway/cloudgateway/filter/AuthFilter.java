package vip.yeee.memo.demo.springcloud.gateway.cloudgateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vip.yeee.memo.base.model.constant.TokenConstants;
import vip.yeee.memo.base.model.rest.ResultCode;
import vip.yeee.memo.common.appauth.client.kit.JwtClientKit;
import vip.yeee.memo.common.appauth.client.properties.ApiAuthClientProperties;
import vip.yeee.memo.demo.springcloud.gateway.cloudgateway.utils.WebFluxResponseUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 网关鉴权
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private ApiAuthClientProperties apiAuthClientProperties;

    @Autowired
    private JwtClientKit jwtClientKit;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StrUtil.isBlank(url)
                || apiAuthClientProperties.getExclude().stream().anyMatch(p -> new AntPathMatcher().match(p, url))) {
                return chain.filter(exchange);
        }
        String token = getJwtToken(request);
        if (StrUtil.isEmpty(token)) {
            return WebFluxResponseUtil.webFluxResponseWriter(exchange.getResponse(), ResultCode.UNAUTHORIZED);
        }
        try {
            Claims claims = jwtClientKit.getTokenClaim(token);
            if (claims == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            return WebFluxResponseUtil.webFluxResponseWriter(exchange.getResponse(), ResultCode.UNAUTHORIZED);
        }
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) throws UnsupportedEncodingException {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = URLEncoder.encode(valueStr, "utf-8");
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    /**
     * 获取请求token
     */
    private String getJwtToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}