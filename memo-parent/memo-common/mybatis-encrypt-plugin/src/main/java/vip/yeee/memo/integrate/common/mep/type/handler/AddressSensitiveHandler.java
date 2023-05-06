package vip.yeee.memo.integrate.common.mep.type.handler;

import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.integrate.common.mep.type.SensitiveType;
import vip.yeee.memo.integrate.common.mep.type.SensitiveTypeHandler;

/**
 * 收货地址脱敏处理类
 * 地址只显示到地区，不显示详细地址；我们要对个人信息增强保护
 * 例子：北京市海淀区****
 */
public class AddressSensitiveHandler implements SensitiveTypeHandler {

    private static final int RIGHT = 10;
    private static final int LEFT = 6;
    @Override
    public SensitiveType getSensitiveType() {
        return SensitiveType.ADDRESS;
    }

    @Override
    public String handle(Object src) {
        if (src == null) {
            return null;
        }
        String address = src.toString();
        int length = StringUtils.length(address);
        if (length > RIGHT + LEFT) {
            return StringUtils.leftPad(StringUtils.left(address, length - RIGHT), length, "*");
        }
        if (length <= LEFT){
            return address;
        } else {
            return address.substring(0, LEFT + 1).concat("*****");
        }

    }
}
