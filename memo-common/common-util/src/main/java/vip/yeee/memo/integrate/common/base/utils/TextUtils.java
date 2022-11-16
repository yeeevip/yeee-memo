package vip.yeee.memo.integrate.common.base.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;

public class TextUtils {

    public static String cleanHtmlTag(String text) {
        if (text == null) {
            return null;
        }
        return StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(text))
                .replaceAll("&nbsp;", "");
    }

}
