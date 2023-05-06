package vip.yeee.memo.integrate.jdk.base.stream;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/5/4 17:41
 */
public class StreamExample {

    public static void main(String[] args) {
//        useFlatMap();
    }

    private static void useFlatMap() {
        List<Integer> l1 = Lists.newArrayList(11);
        List<Integer> l2 = Lists.newArrayList(22);
        List<Integer> l3 = Lists.newArrayList(33);
        Stream.of(l1, l2, l3)
                .flatMap(Collection::stream)
                .forEach(System.out::println);
    }

}
