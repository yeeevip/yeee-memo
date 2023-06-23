package vip.yeee.memo.demo.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/12 15:00
 */
@Slf4j
public class DirectConnectionCanal {

    private static final String CANAL_MONITOR_HOST = "yeee.vip.host";

    private static final Integer CANAL_MONITOR_PORT = 11111;

    private static final String CANAL_MONITOR_DATABASE = "yeeee_manage";

    private final static int BATCH_SIZE = 10000;

    public static void main(String[] args) {
        while (true) {
            CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(CANAL_MONITOR_HOST, CANAL_MONITOR_PORT)
                    , "example", "", "");
            try {
                //打开连接
                connector.connect();
                log.info("数据库检测连接成功!" + CANAL_MONITOR_DATABASE);
                //订阅数据库表,全部表q
                connector.subscribe(CANAL_MONITOR_DATABASE + "\\..*");
                //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                connector.rollback();
                while (true) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(BATCH_SIZE);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                    } else {
                        handleDATAChange(message.getEntries());
                    }
                    // 提交确认
                    connector.ack(batchId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("成功断开监测连接!尝试重连");
            } finally {
                connector.disconnect();
                //防止频繁访问数据库链接: 线程睡眠 10秒
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 打印canal server解析binlog获得的实体类信息
     */
    private static void handleDATAChange(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),e);
            }
            CanalEntry.EventType eventType = rowChange.getEventType();
            log.info("Canal监测到更新:【{}】", entry.getHeader().getTableName());
            log.info("-----------------------------------------------------");
            log.info("rowChange = {}，entry = {}", rowChange, entry);
            switch (eventType) {
                case DELETE:
                    log.info("DELETE");
                    break;
                case INSERT:
                    log.info("INSERT");
                    break;
                case UPDATE:
                    log.info("UPDATE");
                    break;
                default:
                    break;
            }

        }
    }

}
