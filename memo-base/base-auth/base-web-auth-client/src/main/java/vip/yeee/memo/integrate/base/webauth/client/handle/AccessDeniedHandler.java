package vip.yeee.memo.integrate.base.webauth.client.handle;

import org.springframework.security.access.AccessDeniedException;
import vip.yeee.memo.integrate.base.webauth.client.utils.HttpResponseUtils;
import vip.yeee.memo.integrate.common.model.rest.CommonResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理器
 */
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        HttpResponseUtils.write(response, CommonResult.forbidden(""));
    }

}