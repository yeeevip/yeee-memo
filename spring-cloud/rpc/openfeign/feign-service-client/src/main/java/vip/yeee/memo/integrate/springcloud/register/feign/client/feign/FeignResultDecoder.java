package vip.yeee.memo.integrate.springcloud.register.feign.client.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 自定义一个解码器，对feign服务端返回的数据重新包装
 *
 * @author yeeeee
 * @since 2021/12/29 14:49
 */
public class FeignResultDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) {
            throw new DecodeException(response.status(), "没有返回有效的数据", response.request());
        }
        String bodyStr = new BufferedReader(response.body().asReader(StandardCharsets.UTF_8)).lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject resultObj = JSON.parseObject(bodyStr);
        if (resultObj.getInteger("code") != 0) { // code != 0
            if (resultObj.getBoolean("userPrompt")) { // 失败信息是否为用户提示，如果是的话框架不会主动拦截该错误
                throw new DecodeException(response.status(), "接口返回错误：" + resultObj.getString("msg"), response.request());
            }
        }
        return resultObj.getObject("data", JSONObject.class);
    }

}
