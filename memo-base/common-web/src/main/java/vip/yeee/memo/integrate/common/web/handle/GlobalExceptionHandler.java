package vip.yeee.memo.integrate.common.web.handle;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.common.model.rest.CommonResult;
import vip.yeee.memo.integrate.common.model.rest.ResultCode;

import java.lang.reflect.Field;

/**
 * 异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public CommonResult<Object> handleException(Throwable e) {
        log.error("system error", e);
        return CommonResult.failed(ResultCode.SYS_ERROR);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e) {
        log.error("system error", e);
        return CommonResult.failed(ResultCode.SYS_ERROR);
    }

    /**
     * 业务异常 堆栈异常不打印
     */
    @ExceptionHandler(BizException.class)
    public CommonResult<Object> handleBizException(BizException e) {
        log.error("code:{}, message:{}", e.getResultCode().getCode(), e.getMessage());
        return CommonResult.failed(e.getResultCode(), e.getMessage());
    }

    /**
     * 验证异常 JSR303 校验堆栈异常不打印
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<Object> handleBindException(BindException e) {
        log.error(e.getMessage());
        return CommonResult.failed(e.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResult<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<?> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindResult = e.getBindingResult();
        Field[] fields = ReflectUtil.getFields(bindResult.getTarget().getClass());
        String defaultMessage = null;
        for (Field field : fields) {
            for (FieldError error : bindResult.getFieldErrors()) {
                if (field.getName().equalsIgnoreCase(error.getField())) {
                    defaultMessage = error.getDefaultMessage();
                    break;
                }
            }
        }
        // 存在其他未处理的参数校验错误
        boolean hasUnProcessError = bindResult.getFieldError() != null && defaultMessage == null;
        if (hasUnProcessError) {
            defaultMessage = bindResult.getFieldError().getDefaultMessage();
        }
        return CommonResult.failed(defaultMessage);
    }
}