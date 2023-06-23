package vip.yeee.memo.base.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.yeee.memo.base.model.rest.ResultCode;
import vip.yeee.memo.base.model.rest.IErrorCode;

/**
 * 业务异常类信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected final IErrorCode resultCode;

    /**
     * 无参默认构造 UNSPECIFIED
     */
    public BizException() {
        super(ResultCode.FAILED.getMessage());
        this.resultCode = ResultCode.FAILED;
    }

    /**
     * 指定错误码构造通用异常
     *
     * @param resultCode 错误码
     */
    public BizException(final IErrorCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * 指定详细描述构造通用异常
     *
     * @param detailedMessage 详细描述
     */
    public BizException(final String detailedMessage) {
        super(detailedMessage);
        this.resultCode = ResultCode.FAILED;
    }

    /**
     * 指定异常构造通用异常
     *
     * @param t 异常
     */
    public BizException(final Throwable t) {
        super(t);
        this.resultCode = ResultCode.FAILED;
    }

    /**
     * 构造通用异常
     *
     * @param resultCode      错误码
     * @param detailedMessage 详细描述
     */
    public BizException(final IErrorCode resultCode, final String detailedMessage) {
        super(detailedMessage);
        this.resultCode = resultCode;
    }

    /**
     * 构造通用异常
     *
     * @param resultCode 错误码
     * @param t          异常
     */
    public BizException(final IErrorCode resultCode, final Throwable t) {
        super(resultCode.getMessage(), t);
        this.resultCode = resultCode;
    }

    /**
     * 构造通用异常
     *
     * @param detailedMessage 详细描述
     * @param t               异常
     */
    public BizException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.resultCode = ResultCode.FAILED;
    }

    /**
     * 构造通用异常
     *
     * @param resultCode      错误码
     * @param detailedMessage 详细描述
     * @param t               异常
     */
    public BizException(final IErrorCode resultCode, final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.resultCode = resultCode;
    }

    /**
     * errorCode
     *
     * @return
     */
    public IErrorCode getErrorCode() {
        return resultCode;
    }

}