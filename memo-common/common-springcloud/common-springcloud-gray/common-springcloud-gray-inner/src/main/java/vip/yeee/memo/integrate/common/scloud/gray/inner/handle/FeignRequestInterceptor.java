package vip.yeee.memo.integrate.common.scloud.gray.inner.handle;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.yeee.memo.integrate.base.model.constant.CloudGrayConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/6 17:48
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> headers = getHeaders(httpServletRequest);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            //② 设置请求头到新的Request中
            requestTemplate.header(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取原请求头
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                //将灰度标记的请求头透传给下个服务
                if (StrUtil.equals(CloudGrayConstant.GRAY_HEADER, key) && Boolean.TRUE.toString().equals(value)){
                    //① 保存灰度发布的标记
                    map.put(key, value);
                }
            }
        }
        return map;
    }
}
