package vip.yeee.memo.integrate.common.model.rest;

/**
 * API操作码
 */
public enum ResultCode implements IErrorCode {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAILED(500, "操作失败"),
    SYS_ERROR(500, "服务器错误，请联系管理员"),
    SERVICE_UNAVAILABLE(500, "服务不可用"),
    METHOD_ERROR(500, "请求方式有误"),

    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "未登录或token失效"),
    FORBIDDEN(403, "拒绝访问");

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
