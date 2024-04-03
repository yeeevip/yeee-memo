package vip.yeee.memo.demo.distribute.lock.zookeeper.zkClientApi;

import vip.yeee.memo.demo.distribute.lock.zookeeper.factory.ZkClientFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * description ...
 *
 * @author https://www.yeee.vip
 * @since 2021/12/30 10:48
 */

@Slf4j
public class ZkClientApiDemo {

    // 父级节点路径
    private static final String PREFIX_PATH = "/zkClient-test";

    /**
     * 创建持久化节点
     * @author https://www.yeee.vip
     * @since 2021/12/30 10:54
     */
    public void createPersistentNode() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        //zkClient.createPersistent(PREFIX_PATH + "/node1");
        zkClient.createPersistent(PREFIX_PATH + "/node2/node2.0", true);
        //zkClient.createPersistent(PREFIX_PATH + "/node3", false, ZooDefs.Ids.OPEN_ACL_UNSAFE);
        zkClient.createPersistent(PREFIX_PATH + "/node4", new UserConfig("Join", 18));
        zkClient.createPersistent(PREFIX_PATH + "/node5", new UserConfig("Mike", 25), ZooDefs.Ids.OPEN_ACL_UNSAFE);
        zkClient.close();
    }

    /**
     * 创建临时节点，断开连接删除节点
     * @author https://www.yeee.vip
     * @since 2021/12/30 13:34
     */
    public void createEphemeralNode() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        UserConfig john = new UserConfig("Join", 56);
        zkClient.createEphemeral(PREFIX_PATH + "/node7");
        zkClient.createEphemeral(PREFIX_PATH + "/node8", ZooDefs.Ids.OPEN_ACL_UNSAFE);
        zkClient.createEphemeral(PREFIX_PATH + "/node9", john);
        zkClient.createEphemeral(PREFIX_PATH + "/node10", john, ZooDefs.Ids.OPEN_ACL_UNSAFE);
        zkClient.close();
    }

    /**
     * 创建持久化有序节点
     * @author https://www.yeee.vip
     * @since 2021/12/30 13:59
     */
    public void cretePersistentSeqNode() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        UserConfig john = new UserConfig("Join", 56);
        String node1 = zkClient.createPersistentSequential(PREFIX_PATH + "/node8", john);
        String node2 = zkClient.createPersistentSequential(PREFIX_PATH + "/node9", john, ZooDefs.Ids.OPEN_ACL_UNSAFE);
        log.info("node1 = " + node1);
        log.info("node2 = " + node2);
        zkClient.close();
    }

    /**
     * 创建临时有序节点
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:04
     */
    public void createEphemeralSeqNode() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        UserConfig john = new UserConfig("Join", 56);
        String node1 = zkClient.createEphemeralSequential(PREFIX_PATH + "/node10", john);
        String node2 = zkClient.createEphemeralSequential(PREFIX_PATH + "/node11", john, ZooDefs.Ids.OPEN_ACL_UNSAFE);
        log.info("node1 = " + node1);
        log.info("node2 = " + node2);
        zkClient.close();
    }

    /**
     * 设置节点数据
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:06
     */
    public void setNodeData() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        UserConfig john = new UserConfig("Join", 56);
        zkClient.writeData(PREFIX_PATH + "/node5", john);
        zkClient.writeData(PREFIX_PATH + "/node5", john, 3);
        Stat stat = zkClient.writeDataReturnStat(PREFIX_PATH + "/node5", john, 4);
        log.info("stat = " + stat.toString());
        zkClient.close();
    }

    /**
     * 删除节点
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:12
     */
    public void deleteNode() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        zkClient.delete(PREFIX_PATH + "/node5");
        zkClient.delete(PREFIX_PATH + "/node5", 5);
        zkClient.deleteRecursive(PREFIX_PATH + "/node5");
        zkClient.close();
    }

    /**
     * 获取节点数据
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:15
     */
    public void getNodeData() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        UserConfig userConfig1 = zkClient.readData(PREFIX_PATH + "/node4");
        Stat stat = new Stat();
        UserConfig userConfig2 = zkClient.readData(PREFIX_PATH + "/node4", stat);
        UserConfig userConfig3 = zkClient.readData(PREFIX_PATH + "/node4", true);
        log.info("userConfig1 = {}，userConfig2 = {}、stat = {}，userConfig3 = {}", userConfig1, userConfig2, stat, userConfig3);
        zkClient.close();
    }

    /**
     * 设置节点权限
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:22
     */
    public void setNodeAcl() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        zkClient.setAcl(PREFIX_PATH + "/node4", ZooDefs.Ids.OPEN_ACL_UNSAFE);
        zkClient.close();
    }

    /**
     * 获取节点的子节点
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:25
     */
    public void getNodeChildren() {
        ZkClient zkClient = ZkClientFactory.getZkClient();
        List<String> children = zkClient.getChildren(PREFIX_PATH);
        zkClient.addAuthInfo("digest", "admin:123456".getBytes(StandardCharsets.UTF_8));
        log.info("children = {}", children);
        zkClient.close();
    }

    /**
     * 测试watch机制
     * @author https://www.yeee.vip
     * @since 2021/12/30 14:30
     */
    public void nodeWatch() throws InterruptedException {

        // 创建子节点变化监听器
        IZkChildListener childListener = new IZkChildListener() {

            /**
             * 子节点变化实际回调方法
             * @param s 节点路径
             * @param list 变化后的子节点列表
             * @author https://www.yeee.vip
             * @since 2021/12/30 14:36
             */
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                log.info("s = {}，list = {}", s, list);
            }
        };

        // 子节点数据变化监听器
        IZkDataListener dataListener = new IZkDataListener() {

            /**
             * 节点数据发生改变回调方法
             * @param s 节点路径
             * @param o 修改后的数据
             * @author https://www.yeee.vip
             * @since 2021/12/30 14:39
             */
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                log.info("s = {}，o = {}", s, o);
            }

            /**
             * 节点删除事件回调方法
             * @author https://www.yeee.vip
             * @since 2021/12/30 14:40
             */
            @Override
            public void handleDataDeleted(String s) throws Exception {
                log.info("s = {}", s);
            }
        };

        ZkClient zkClient = ZkClientFactory.getZkClient();
        zkClient.subscribeChildChanges(PREFIX_PATH, childListener);
        zkClient.subscribeDataChanges(PREFIX_PATH + "/node4", dataListener);

        //zkClient.unsubscribeChildChanges(PREFIX_PATH, childListener);
        //zkClient.unsubscribeDataChanges(PREFIX_PATH + "/node4", dataListener);

        new CountDownLatch(1).await();

    }

    public static void main(String[] args) throws InterruptedException {
        ZkClientApiDemo zkClientApiDemo = new ZkClientApiDemo();
        zkClientApiDemo.nodeWatch();
    }

    @Data
    @AllArgsConstructor
    static class UserConfig implements Serializable {
        private String name;
        private Integer age;
    }

}
