package vip.yeee.memo.integrate.simpletool.my;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2020/12/4 13:35
 */
public class OnlineInterviewBizException extends Exception {
    public OnlineInterviewBizException() {
    }

    private String code = "201";
    private BizErrEnum bizErrEnum;

    public OnlineInterviewBizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public OnlineInterviewBizException(BizErrEnum bizErrEnum) {
        super(bizErrEnum.getMsg());
        this.code = bizErrEnum.getCode();
    }

    public OnlineInterviewBizException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BizErrEnum getBizErrEnum() {
        return bizErrEnum;
    }

    public void setBizErrEnum(BizErrEnum bizErrEnum) {
        this.bizErrEnum = bizErrEnum;
    }

    /**
     * @Author: yeee一页
     * @Date: 2021/7/8 16:46
     */
    public enum BizErrEnum {
        COMMON_ERR_MSG("201", "操作不成功"),
        NOT_OPR_POWER("401", "没有调用权限"),
        LINK_NOT_EXISTS("501", "没有找到链接记录"),
        LINK_HAS_EFFECTIVE("502", "链接已失效"),
        LINK_HAS_SUBMIT("503", "链接已回收，无法修改"),
        ;
        private String code;
        private String msg;

        BizErrEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String desc) {
            this.msg = desc;
        }
    }
}
