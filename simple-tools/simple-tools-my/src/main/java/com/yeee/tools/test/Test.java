package com.yeee.tools.test;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;

import java.nio.charset.Charset;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/14 15:37
 */
public class Test {

    public static void main(String[] args) {
        CsvWriter writer = CsvUtil.getWriter("C:\\Users\\yeeee\\Desktop\\temp\\JmeterCase\\xhyd-case\\userbook.csv", Charset.defaultCharset(), false);
        for (int i = 0; i <= 20000; i++) {
            writer.writeLine(999000010 + i + "", 1531951784446693377L + "");
        }
        writer.flush();
        writer.close();
    }

}
