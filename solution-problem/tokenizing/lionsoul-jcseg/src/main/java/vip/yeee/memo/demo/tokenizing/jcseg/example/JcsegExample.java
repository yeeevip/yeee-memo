package vip.yeee.memo.demo.tokenizing.jcseg.example;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.extra.tokenizer.Word;
import vip.yeee.memo.demo.tokenizing.jcseg.config.ADictionaryExtra;

import java.util.Iterator;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/25 16:32
 */
public class JcsegExample {

    public static void main(String[] args) {
        Iterator<Word> it;String seg;
        String cc = "这 是\n一段演示语句。";
        it = ADictionaryExtra.engine.parse(cc);
        if(IterUtil.isNotEmpty(it)) {
            while(it.hasNext()) {
                seg = it.next().getText();
                System.out.println(seg);
            }
        }
    }

}
