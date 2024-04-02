package vip.yeee.memo.demo.flink.job;

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
import vip.yeee.memo.demo.flink.model.AccessLogBO;
import vip.yeee.memo.demo.flink.process.AccessEventProcessWindowFunction;
import vip.yeee.memo.demo.flink.convert.LogDataConvert;
import vip.yeee.memo.demo.flink.sink.NginxLogComputeSink;

import java.util.Objects;
import java.util.Properties;

/**
 * description......
 *
 * @author https://www.yeee.vip
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
        properties.setProperty("bootstrap.servers","http://yeee.vip.host:21661");
        properties.setProperty("group.id", "vip-yeee-memo");
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>("TP_www.yeee.vip", new SimpleStringSchema(), properties);
        DataStream<AccessLogBO> dataStreamSource = env.addSource(kafkaSource)
                .map(LogDataConvert::nginxLog2AccessBO)
                .filter(Objects::nonNull)
                // 格式转换
                .returns(TypeInformation.of(AccessLogBO.class));
        dataStreamSource
                // 分流
                .keyBy(bo -> bo.getEvent() + ":" + DateUtil.format(bo.getTimestamp(), "yyyyMMddHHmm"))
                // 使用【滚动窗口】滚动窗口：有一个固定的大小且元素[不重叠]
                .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
                // 【窗口触发器】
                // 窗口默认只会在创建结束的时候触发一次计算，然后输出结果
                // 如果长时间的窗口，比如：一天的窗口，要是等到一天结束在输出结果，那还不如跑批。
                // 所以大窗口会添加trigger，以一定的频率输出中间结果。60s触发一次计算，更新统计结果
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(60)))
                // 【窗口剔除器】
                // 每次trigger，触发计算是，窗口中的所有数据都会参与，所以数据会触发很多次，比较浪费，
                // 加evictor 驱逐已经计算过的数据，就不会重复计算了
                .evictor(TimeEvictor.of(Time.seconds(0), true))
                .process(new AccessEventProcessWindowFunction())
                .addSink(new NginxLogComputeSink());
        env.execute("POINT_STATS-www.yeee.vip");
    }

}
