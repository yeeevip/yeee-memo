package vip.yeee.memo.integrate.flink.job;

import cn.hutool.core.date.DateUtil;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import vip.yeee.memo.integrate.flink.convert.LogDataConvert;
import vip.yeee.memo.integrate.flink.model.AccessLogBO;
import vip.yeee.memo.integrate.flink.process.AccessEventProcessWindowFunction;
import vip.yeee.memo.integrate.flink.sink.NginxLogComputeSink;

import java.util.Objects;
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
//        // 容错机制
//        env.enableCheckpointing(5000);
//        env.getCheckpointConfig().setCheckpointStorage("file:///d/ckp-dir");
//        // 算子并行度
//        env.setParallelism(1);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","http://121.4.254.235:21661");
        properties.setProperty("group.id", "vip-yeee-memo");
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>("TP_www.yeee.vip", new SimpleStringSchema(), properties);
        DataStream<AccessLogBO> dataStreamSource = env.addSource(kafkaSource)
                .map(LogDataConvert::nginxLog2AccessBO)
                .filter(Objects::nonNull)
                .returns(TypeInformation.of(AccessLogBO.class));
        dataStreamSource
                .keyBy(bo -> bo.getEvent() + ":" + DateUtil.format(bo.getTimestamp(), "yyyyMMddHHmm"))
                // 滚动窗口有一个固定的大小且元素不重叠
                .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
                // 60s触发一次计算，更新统计结果
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(60)))
                .evictor(TimeEvictor.of(Time.seconds(0), true))
                .process(new AccessEventProcessWindowFunction())
                .addSink(new NginxLogComputeSink());
        env.execute("POINT_STATS-www.yeee.vip");
    }

}
