package vip.yeee.memo.common.encrypt.mybatis.type.handler;

import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveTypeHandler;
import vip.yeee.memo.common.encrypt.mybatis.type.SensitiveType;

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
