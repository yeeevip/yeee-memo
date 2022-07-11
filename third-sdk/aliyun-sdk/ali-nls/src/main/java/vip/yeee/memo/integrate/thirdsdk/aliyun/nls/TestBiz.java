package vip.yeee.memo.integrate.thirdsdk.aliyun.nls;

import cn.hutool.system.SystemUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.base.utils.TextUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/11 18:46
 */
@Slf4j
@Component
public class TestBiz implements ApplicationRunner {

    @Resource
    private AliyunNlsKit aliyunNlsKit;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String content = "";
        Stopwatch stopwatch = Stopwatch.createStarted();
        String fileName = "1111111.mp3";
        String localPath = System.getProperties().getProperty(SystemUtil.TMPDIR) + fileName;
        AudioGenBo audioGenBo = new AudioGenBo(1111111, "content", TextUtils.cleanHtmlTag(content), fileName);
        aliyunNlsKit.text2LocalWavFile(localPath, audioGenBo);
        log.info("【文本转录音】 完成 - 耗时：{}, localPath = {}, ossPath = {}", stopwatch.elapsed(TimeUnit.MILLISECONDS), localPath, fileName);
    }
}
