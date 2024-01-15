package vip.yeee.memo.demo.simpletool.my.temp;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;

import java.nio.charset.Charset;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/1/15 10:21
 */
public class TextFileHandle {

    public static void main(String[] args) {


        List<String> list = FileUtil.readLines("C:\\Workspaces\\private\\mytools\\server_start\\wwwyeeevip\\yeee-application\\plugins\\lexicon\\lex-sensitive.lex", Charset.defaultCharset());
        System.out.println(list.size());
        List<String> list2 = Lists.newArrayList();
        for (String s : list) {
            if (!list2.contains(s)) {
                list2.add(s);
            }
        }
        System.out.println(list2.size());

        FileUtil.writeLines(list2, "C:\\Workspaces\\private\\mytools\\server_start\\wwwyeeevip\\yeee-application\\plugins\\lexicon\\lex-sensitive2.lex", Charset.defaultCharset());

    }
}
