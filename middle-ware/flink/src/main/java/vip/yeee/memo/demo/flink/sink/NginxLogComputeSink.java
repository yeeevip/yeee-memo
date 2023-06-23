package vip.yeee.memo.demo.flink.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import vip.yeee.memo.demo.flink.model.AccessStatsVO;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/2 10:59
 */
@Slf4j
public class NginxLogComputeSink extends RichSinkFunction<AccessStatsVO> {

    public NginxLogComputeSink() {
        log.info("DemoSink new");
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        log.info("NginxLogComputeSink open");
    }

    @Override
    public void invoke(AccessStatsVO value, Context context) throws Exception {
        try {
            log.info("NginxLogComputeSink invoke ---> value = {}", value);
        } catch (Exception e) {
            log.error("NginxLogComputeSink invoke error ---> value = {}", value);
        }
    }

    @Override
    public void close() throws Exception {
        log.info("NginxLogComputeSink close");
    }

}
