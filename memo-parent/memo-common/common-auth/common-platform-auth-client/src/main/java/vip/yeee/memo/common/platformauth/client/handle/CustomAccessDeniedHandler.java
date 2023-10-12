package vip.yeee.memo.common.platformauth.client.handle;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.common.platformauth.client.utils.HttpResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理器
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        HttpResponseUtils.write(response, CommonResult.forbidden(""));
    }

}
