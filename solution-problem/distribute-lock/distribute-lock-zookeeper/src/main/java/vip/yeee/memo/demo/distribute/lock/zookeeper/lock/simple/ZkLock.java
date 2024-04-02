package vip.yeee.memo.demo.distribute.lock.zookeeper.lock.simple;

import vip.yeee.memo.demo.distribute.lock.zookeeper.factory.ZkClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 顺序性，第一个创建节点的抢锁成功
 *
 * @author https://www.yeee.vipe
 * @since 2021/12/29 18:28
 */
@Slf4j
public class ZkLock implements Lock {

    // ZkLock的节点链接
    private static final String ZK_PATH = "/test/lock";
    private static final String LOCK_PREFIX = ZK_PATH + "/";
    private static final long WAIT_TIME = 1000;

    // ZK 客户端
    private ZkClient client = null;

    private String locked_short_path = null;
    private String locked_path = null;
    private String prior_path = null;
    private final AtomicInteger lockCount = new AtomicInteger(0);
    private Thread thread;

    public ZkLock() {
        client = ZkClientFactory.getZkClient();
        synchronized (client) {
            if (!client.exists(ZK_PATH)) {
                client.createPersistent(ZK_PATH, true);
            }
        }
    }

    @Override
    public boolean lock() throws Exception {
        // 可重入，确保同一线程可以重复加锁
        synchronized (this) {
            if (lockCount.get() == 0) {
                thread = Thread.currentThread();
                lockCount.incrementAndGet();
            } else {
                if (!thread.equals(Thread.currentThread())) {
                    return false;
                } else {
                    lockCount.incrementAndGet();
                    return  true;
                }
            }
        }

        try {
            boolean locked = false;

            // 尝试着去加锁
            locked = tryLock();

            if (locked) {
                return true;
            }

            // 如果加锁失败就去等待
            while (!locked) {
                await();
                // 获取等待的子节点列表
                List<String> waiters = getWaiters();
                // 判断是否加锁成功
                if (checkLocked(waiters)) {
                    locked = true;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            unlock();
        }
        return false;
    }

    /**
     * 监听前一个Znode节点（prior_path成员）的删除事件
     * @author https://www.yeee.vipee
     * @since 2021/12/30 16:37
     */
    private void await() throws Exception {
        if (prior_path == null) {
            throw new Exception("prior_path error");
        }
        final CountDownLatch latch = new CountDownLatch(1);

        // 订阅比自己次小顺序节点的删除事件
        client.subscribeDataChanges(prior_path, new IZkDataListener() {

            @Override
            public void handleDataChange(String s, Object o) throws Exception {}

            @Override
            public void handleDataDeleted(String s) throws Exception {
                log.info("节点{}删除", s);
                latch.countDown();
            }
        });

        latch.await(WAIT_TIME, TimeUnit.SECONDS);
    }

    private boolean checkLocked(List<String> waiters) {
        // 节点按照编号升序排列
        Collections.sort(waiters);
        // 如果是第一个，代表自己已经获得了锁
        if (locked_short_path.equals(waiters.get(0))) {
            log.info("成功的获取分布式锁，节点为{}", locked_short_path);
            return true;
        }
        return false;
    }

    private boolean tryLock() throws Exception {
        // 创建临时节点Znode
        locked_path = client.createEphemeralSequential(LOCK_PREFIX, null);
        if (locked_path == null) {
            throw new Exception("zk error");
        }
        // 获取所有节点
        List<String> waiters = getWaiters();
        // 取得加锁的排队编号
        locked_short_path = getShortPath(locked_path);

        // 获取等待的子节点列表，判断自己是否是第一个
        if (checkLocked(waiters)) {
            return true;
        }

        // 判断自己怕排第几个
        int index = Collections.binarySearch(waiters, locked_short_path);
        if (index < 0) { // 网络抖动，获取到的子节点列表里可能已经没有自己了
            throw new Exception("节点没有找到：" + locked_short_path);
        }

        // 如果自己没有获取到锁，则要监听前一个节点
        prior_path = ZK_PATH + "/" + waiters.get(index - 1);
        return false;
    }

    private List<String> getWaiters() {
        return client.getChildren(ZK_PATH);
    }

    private String getShortPath(String lockedPath) {
        int index = lockedPath.lastIndexOf(ZK_PATH + "/");
        if (index >= 0) {
            index += ZK_PATH.length() + 1;
            return index <= lockedPath.length() ? lockedPath.substring(index) : "";
        }
        return null;
    }

    @Override
    public boolean unlock() {
        // 只有加锁的线程能够解锁
        if (!thread.equals(Thread.currentThread())) {
            return false;
        }
        // 减少可重入的次数
        int newLockCount = lockCount.decrementAndGet();
        if (newLockCount < 0) {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock：" + locked_path);
        }
        // 如果计数不为0，直接返回
        if (newLockCount != 0) {
            return true;
        }
        // 删除临时节点
        if (client.exists(locked_path)) {
            client.delete(locked_path);
        }
        return true;
    }

}
