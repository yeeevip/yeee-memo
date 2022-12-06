package vip.yeee.memo.integrate.common.scloud.gray.inner.handle;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import vip.yeee.memo.integrate.base.model.constant.CloudGrayConstant;
import vip.yeee.memo.integrate.common.scloud.gray.inner.context.GrayRequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/6 18:09
 */
public class GrayWebMvcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String garyTag = request.getHeader(CloudGrayConstant.GRAY_HEADER);
        if (StrUtil.isNotBlank(garyTag)) {
            GrayRequestContextHolder.setGrayTag(true);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        GrayRequestContextHolder.remove();
    }
}
