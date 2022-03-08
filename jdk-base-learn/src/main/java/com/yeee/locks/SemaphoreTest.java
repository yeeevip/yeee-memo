package com.yeee.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 假若一个工厂有 5 台机器，但是有 8 个工人，一台机器同时只能被一个工人使用，只有使用完了，其他工人才能继续使用
 *
 * @author yeeeee
 * @since 2022/3/8 17:25
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        int N = 8; // 8个工人
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < N; i++) {
            new Worker(i, semaphore).start();
        }
    }

}

@Slf4j
class Worker extends Thread {
    private int num;
    private Semaphore semaphore;

    public Worker(int num, Semaphore semaphore) {
        this.num = num;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            log.info("工人 " + this.num + " 占用一个机器在生产...");
            TimeUnit.SECONDS.sleep(2);
            log.info("工人 " + this.num + " 释放出机器");
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}