package vip.yeee.memo.integrate.thirdsdk.aliyun.oss;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.yeee.memo.integrate.thirdsdk.aliyun.oss.kit.AliyunOssKit;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 16:56
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AliOssTest {

    @Resource
    private AliyunOssKit aliyunOssKit;

    @Test
    public void testUpload() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (1).mp3";
        try {
            aliyunOssKit.uploadObject(FileUtil.file("C:\\Users\\yeeee\\Desktop\\9976014 (1).mp3"), objectName);
        } catch (IOException e) {
            log.error("aliyun oss 上传失败 - objectName = {}", objectName, e);
        }
    }

    @Test
    public void testUploadNoMetadata() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (2).mp3";
        aliyunOssKit.uploadObjectNoMetadata(FileUtil.getInputStream("C:\\Users\\yeeee\\Desktop\\9976014 (1).mp3"), objectName);
    }

    @Test
    public void testDownload() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (1).mp3";
        InputStream inputStream = aliyunOssKit.getObject(objectName);
        IoUtil.copy(inputStream, FileUtil.getOutputStream("C:\\Users\\yeeee\\Desktop\\temp\\download\\9976014 (1).mp3"));
    }

}
