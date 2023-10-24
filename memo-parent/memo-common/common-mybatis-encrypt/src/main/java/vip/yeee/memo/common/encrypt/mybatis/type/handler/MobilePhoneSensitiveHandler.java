package vip.yeee.memo.common.encrypt.mybatis.type.handler;

import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveTypeHandler;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveType;

/**
 * 手机号脱敏处理类
 * 18233583070 脱敏后: 182****3030
 */
public class MobilePhoneSensitiveHandler implements SensitiveTypeHandler {
    @Override
    public SensitiveType getSensitiveType() {
        return SensitiveType.MOBILE_PHONE;
    }

    @Override
    public String handle(Object src) {
        if(src == null){
            return null;
        }
        String value = src.toString();
        return StringUtils.left(value, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(value, 4), StringUtils.length(value), "*"), "***"));
    }

}
