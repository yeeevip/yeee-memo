package vip.yeee.memo.demo.jdk.base.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行
 * 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier 可以被重用。我们暂且把这个状态就叫做 barrier，当调用 await() 方法之后，线程就处于 barrier 了
 *
 * @author https://www.yeee.vipe
 * @since 2022/3/8 16:38
 */
@Slf4j
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                log.info(Thread.currentThread().getName() + " 随便选择一个线程执行！");
            }
        });
        for (int i = 0; i < 4; i++) {
            new Write(cyclicBarrier).start();
        }
    }

}

@Slf4j
class Write extends Thread {

    private final CyclicBarrier cyclicBarrier;

    public Write(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        log.info(Thread.currentThread().getName() + " 正在写入数据...");
        try {
            TimeUnit.SECONDS.sleep(1);
            log.info(Thread.currentThread().getName() + " 写入数据完毕，等待其他线程写入...");
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        log.info(Thread.currentThread().getName() + " 线程写入完毕，继续执行其他任务");
    }
}
