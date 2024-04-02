package vip.yeee.memo.demo.jdk.base.problem;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * -Xms1m -Xmx1m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=C:/Users/yeeee/Desktop/新建文件夹
 *
 * @author https://www.yeee.vip
 * @since 2022/10/20 15:12
 */
public class OutOfMemoryTest {

    public static void main(String[] args) {
        List<Byte[]> list = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            list.add(new Byte[1024 * 1024 * 1]);
        }
        System.out.println(list);
    }

}
