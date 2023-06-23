package vip.yeee.memo.demo.stools.kit;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.common.httpclient.okhttp.kit.OkHttp3Kit;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/6 11:17
 */
@Slf4j
@SpringBootTest
public class OkHttp3Tests {

    @Resource
    private OkHttp3Kit okHttp3Kit;

    @Test
    public void testRequestGet() {
        String res = okHttp3Kit.get("http://127.0.0.1:8080/stools/openapi/v1?username=yeee");
        log.info("res = {}", res);
    }

    @Test
    public void testRequestGetCustom() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        ThreadUtil.execAsync(() -> {
            try {
                log.info("[执行指定超时60s的client] - 开始");
                String res = okHttp3Kit.builderClient(builder -> {
                    return builder.connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
                            .readTimeout(60, TimeUnit.SECONDS);
                }).get("http://127.0.0.1:8080/stools/openapi/v1?username=yeee");
                log.info("[执行指定超时60s的client] - 结束 res = {}", res);
            } catch (Exception e) {
                log.error("[执行指定超时60s的client] - 错误", e);
            } finally {
                countDownLatch.countDown();
            }
        });
        TimeUnit.MILLISECONDS.sleep(500);
        ThreadUtil.execAsync(() -> {
            try {
                log.info("[执行默认超时1的client] - 开始");
                String res = okHttp3Kit.get("http://127.0.0.1:8080/stools/openapi/v1?username=yeee");
                log.info("[执行默认超时1的client] - 结束 res = {}", res);
            } catch (Exception e) {
                log.error("[执行默认超时1的client] - 错误", e);
            } finally {
                countDownLatch.countDown();
            }
        });
        ThreadUtil.execAsync(() -> {
            try {
                log.info("[执行指定超时10s的client] - 开始");
                String res = okHttp3Kit.builderClient(builder -> {
                    return builder.readTimeout(10, TimeUnit.SECONDS);
                }).get("http://127.0.0.1:8080/stools/openapi/v1?username=yeee");
                log.info("[执行指定超时10s的client] - 结束 res = {}", res);
            } catch (Exception e) {
                log.error("[执行指定超时10s的client] - 错误", e);
            } finally {
                countDownLatch.countDown();
            }
        });
        TimeUnit.MILLISECONDS.sleep(500);
        ThreadUtil.execAsync(() -> {
            try {
                log.info("[执行默认超时2的client] - 开始");
                String res = okHttp3Kit.get("http://127.0.0.1:8080/stools/openapi/v1?username=yeee");
                log.info("[执行默认超时2的client] - 结束 res = {}", res);
            } catch (Exception e) {
                log.error("[执行默认超时2的client] - 错误", e);
            } finally {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

}
