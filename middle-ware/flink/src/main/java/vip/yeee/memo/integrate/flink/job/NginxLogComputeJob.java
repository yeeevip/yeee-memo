package vip.yeee.memo.integrate.flink.job;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import vip.yeee.memo.integrate.flink.sink.DemoSink;

import java.util.Properties;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/2 13:50
 */
public class NginxLogComputeJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","http://121.4.254.235:21661");
        properties.setProperty("group.id", "vip-yeee-memo");
        DataStreamSource<String> stream = env.addSource(new FlinkKafkaConsumer<>("topic_log", new SimpleStringSchema(), properties));
        stream.addSink(new DemoSink());
        env.execute("flink-nginx-log-compute");
    }

}
