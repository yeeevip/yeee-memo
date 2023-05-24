package vip.yeee.memo.integrate.base.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义过滤器
 *
 * @author yeeee
 * @since 2022/7/12 14:25
 */
@Slf4j
public class FilterDemo implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("【自定义过滤器】 - 执行前 - uri = {}", ((HttpServletRequest)servletRequest).getRequestURI());
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("【自定义过滤器】 - 执行后 - uri = {}", ((HttpServletRequest)servletRequest).getRequestURI());
    }

}
