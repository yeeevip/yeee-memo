package vip.yeee.memo.integrate.common.mep.type.handler;

import vip.yeee.memo.integrate.common.mep.type.SensitiveType;
import vip.yeee.memo.integrate.common.mep.type.SensitiveTypeHandler;

/**
 * 不脱敏
 */
public class NoneSensitiveHandler implements SensitiveTypeHandler {
    @Override
    public SensitiveType getSensitiveType() {
        return SensitiveType.NONE;
    }

    @Override
    public String handle(Object src) {
        if(src != null) {
            return src.toString();
        }
        return null;
    }
}
