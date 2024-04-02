package vip.yeee.memo.demo.springcloud.gateway.cloudgateway.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.base.model.rest.IErrorCode;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/9/11 14:22
 */
public class WebFluxResponseUtil {

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param code        响应状态码
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, IErrorCode code) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        CommonResult<?> result = CommonResult.failed(code);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
