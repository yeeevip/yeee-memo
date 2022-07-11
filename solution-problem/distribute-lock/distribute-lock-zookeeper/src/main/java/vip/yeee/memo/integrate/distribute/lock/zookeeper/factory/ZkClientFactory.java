package vip.yeee.memo.integrate.distribute.lock.zookeeper.factory;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/30 10:41
 */
@Slf4j
public class ZkClientFactory {

    // zookeeper的服务器地址，集群的话用逗号分隔
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static ZkClient zkClient = null;

    public static ZkClient getZkClient() {
        if (zkClient != null) return zkClient;
        synchronized (ZkClientFactory.class) {
            if (zkClient != null) return zkClient;
            zkClient = new ZkClient(ZK_ADDRESS, 3000);
            log.info("连接zookeeper服务端成功");
            return zkClient;
        }
    }

}
