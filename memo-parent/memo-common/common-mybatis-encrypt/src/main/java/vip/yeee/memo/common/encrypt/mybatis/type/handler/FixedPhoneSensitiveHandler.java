package vip.yeee.memo.common.encrypt.mybatis.type.handler;

import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveTypeHandler;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveType;

/**
 * 座机信息脱敏
 * 座机的前2位和后4位保留，其余的隐藏。
 */
public class FixedPhoneSensitiveHandler implements SensitiveTypeHandler {
    @Override
    public SensitiveType getSensitiveType() {
        return SensitiveType.FIXED_PHONE;
    }

    @Override
    public String handle(Object src) {
        if(src==null){
            return null;
        }
        String fixedPhone=src.toString();
        return StringUtils.left(fixedPhone, 2).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(fixedPhone, 4), StringUtils.length(fixedPhone), "*"), "***"));
    }
}
