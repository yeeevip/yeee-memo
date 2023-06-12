package vip.yeee.memo.integrate.thirdsdk.aliyun.oss;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.integrate.common.oss.kit.AliOssKit;

import javax.annotation.Resource;
import java.io.*;
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
public class AliOssTest {

    @Resource
    private AliOssKit aliOssKit;

    @Test
    public void testUpload() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (1).mp3";
        try {
            aliOssKit.uploadObject(FileUtil.file("C:\\Users\\yeeee\\Desktop\\9976014 (1).mp3"), objectName);
        } catch (IOException e) {
            log.error("aliyun oss 上传失败 - objectName = {}", objectName, e);
        }
    }

    @Test
    public void testUploadNoMetadata() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (2).mp3";
        aliOssKit.uploadObjectNoMetadata(FileUtil.getInputStream("C:\\Users\\yeeee\\Desktop\\9976014 (1).mp3"), objectName);
    }

    @Test
    public void testDownload() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (1).mp3";
        InputStream inputStream = aliOssKit.getObject(objectName);
        IoUtil.copy(inputStream, FileUtil.getOutputStream("C:\\Users\\yeeee\\Desktop\\temp\\download\\9976014 (1).mp3"));
    }

    @Test
    public void testUploadByLocalFile() {
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String datePath = StrUtil.format("{}/{}/{}", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String objectName = "yeee/test/upload/mp3/" + datePath + "/9976014 (1).mp3";
        String localPath = System.getProperties().getProperty(SystemUtil.TMPDIR) + objectName;
        try (BufferedOutputStream out = FileUtil.getOutputStream(localPath)) {
            InputStream inputStream = new ByteArrayInputStream("数据输入流".getBytes());
            IoUtil.copy(inputStream, out);
            aliOssKit.uploadObject(FileUtil.getInputStream(localPath), objectName);
        } catch (Exception e) {
            log.error("aliyun oss 上传失败 - objectName = {}", objectName, e);
        } finally {
            FileUtil.del(localPath);
        }
    }

}
