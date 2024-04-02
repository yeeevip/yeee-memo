package vip.yeee.memo.demo.jdk.base.collection;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description ...
 *
 * @author https://www.yeee.vipe
 * @since 2022/2/14 17:12
 */
@Slf4j
public class ForEachTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < 20; i++) {
            list.add(String.valueOf(i + 1));
        }

        // for
        for (int i = 0 ; i < list.size(); i++) {

        }

        // for each，本质是iterator while hasNext
        for (String s : list) {

        }

        // iterator
        list.iterator().forEachRemaining(s -> {});

        // forEach，实现是for each
        list.forEach(s -> {});

        // spliterator，可以将集合分隔用多线程迭代
        ExecutorService executorService = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100)/*可以不设置size，无界 */);
        Spliterator<String> spliterator = list.spliterator();
        Spliterator<String> s1 = spliterator.trySplit();
        Spliterator<String> s2 = spliterator.trySplit();
        executorService.execute(() -> spliterator.forEachRemaining(s -> System.out.println(s + "->t1")));
        executorService.execute(() -> s1.forEachRemaining(s -> System.out.println(s + "->t2")));
        executorService.execute(() -> s2.forEachRemaining(s -> System.out.println(s + "->t3")));
        executorService.shutdown();

        // 删除后size不变
        for (int i = 0 ; i < list.size(); i++) {
            list.remove(i);
        }
        System.out.println(list.size());

        // 删除元素正确方式 【iterator.remove()】，删除后size为0
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()) {
            String s = (String)iterator.next();
            iterator.remove();
            // 错误的方式
            list.remove(s);
        }
        //list.removeIf();
        System.out.println(list.size());

        log.info("--------------> end <-----------------");

    }

}
