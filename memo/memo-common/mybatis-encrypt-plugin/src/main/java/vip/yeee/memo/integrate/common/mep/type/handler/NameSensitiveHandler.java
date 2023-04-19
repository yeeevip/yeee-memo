package vip.yeee.memo.integrate.common.mep.type.handler;

import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.integrate.common.mep.type.SensitiveType;
import vip.yeee.memo.integrate.common.mep.type.SensitiveTypeHandler;

/**
 * 姓名脱敏的解析类
 * 中文姓名只显示第一个汉字，其他隐藏为2个星号
 * 例子：李**
 * 张三丰 ：张**
 */
public class NameSensitiveHandler implements SensitiveTypeHandler {
    @Override
    public SensitiveType getSensitiveType() {
        return SensitiveType.CHINESE_NAME;
    }

    @Override
    public String handle(Object src) {
        if (src == null) {
            return "";
        }
        String fullName = src.toString();
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }
}
