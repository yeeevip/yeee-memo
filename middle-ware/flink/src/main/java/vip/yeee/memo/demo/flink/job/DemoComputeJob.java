package vip.yeee.memo.demo.flink.job;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import vip.yeee.memo.demo.flink.sink.DemoSink;

import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/2 11:01
 */
public class DemoComputeJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 有限的流
//        DataStreamSource<String> stream = env.fromElements("1", "2", "3");
        // 这里模拟一个持续发送数据的源
        DataStreamSource<String> stream = env.addSource(new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                long c = 0;
                while (true) {
                    sourceContext.collect("test" + c++);
                    TimeUnit.SECONDS.sleep(2);
                }
            }

            @Override
            public void cancel() {

            }
        });
        stream.addSink(new DemoSink());
        env.execute("flink demo");
    }

}
