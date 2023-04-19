package vip.yeee.memo.integrate.jdk.base.problem;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/10/20 13:31
 */
public class DeadLockTest {

    private final static Lock lock1 = new ReentrantLock();
    private final static Lock lock2 = new ReentrantLock();
    private final static Object obj1 = new Object();
    private final static Object obj2 = new Object();

    public static void main(String[] args) {
//        exec1();
        exec2();
    }

    private static void exec1() {
        ThreadUtil.execAsync(DeadLockTest::task1);
        ThreadUtil.execAsync(DeadLockTest::task2);
    }

    private static void exec2() {
        ThreadUtil.execAsync(DeadLockTest::task3);
        ThreadUtil.execAsync(DeadLockTest::task4);
    }

    private static void task1() {
        lock1.lock();
        try {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
            try {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock2.unlock();
            }
        } finally {
            lock1.unlock();
        }
    }

    private static void task2() {
        lock2.lock();
        try {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
            try {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock1.unlock();
            }
        } finally {
            lock2.unlock();
        }
    }

    private static void task3() {
        synchronized (obj1) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (obj2) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void task4() {
        synchronized (obj2) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (obj1) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
