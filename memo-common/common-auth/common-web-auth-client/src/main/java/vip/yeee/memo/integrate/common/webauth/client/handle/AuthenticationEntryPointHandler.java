package vip.yeee.memo.integrate.common.webauth.client.handle;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.common.webauth.client.utils.HttpResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理器
 */
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        HttpResponseUtils.write(response, CommonResult.unauthorized(""));
    }

}