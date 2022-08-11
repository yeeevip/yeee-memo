package vip.yeee.memo.integrate.thirdsdk.aliyun.nls;

import cn.hutool.system.SystemUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.integrate.common.base.utils.TextUtils;
import vip.yeee.memo.integrate.thirdsdk.aliyun.nls.bo.AudioGenBo;
import vip.yeee.memo.integrate.thirdsdk.aliyun.nls.kit.AliyunNlsHelper;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 9:14
 */
@Slf4j
@SpringBootTest
public class TestNls {

    @Resource
    private AliyunNlsHelper aliyunNlsHelper;

    @Test
    public void testGenAudio() throws Exception {
        String content = "测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本" +
                "，测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本" +
                "，测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本" +
                "，测试文本测试文本测试文本测试文本测试文本测试文本测试文本测试文本";
        Stopwatch stopwatch = Stopwatch.createStarted();
        String fileName = "genAudio/1111111.mp3";
        String localPath = System.getProperties().getProperty(SystemUtil.TMPDIR) + fileName;
        AudioGenBo audioGenBo = new AudioGenBo(1111111, "content", TextUtils.cleanHtmlTag(content), fileName);
        aliyunNlsHelper.text2LocalFile(localPath, audioGenBo);
        log.info("【文本转录音】 完成 - 耗时：{}, localPath = {}", stopwatch.elapsed(TimeUnit.MILLISECONDS), localPath);
    }

}
