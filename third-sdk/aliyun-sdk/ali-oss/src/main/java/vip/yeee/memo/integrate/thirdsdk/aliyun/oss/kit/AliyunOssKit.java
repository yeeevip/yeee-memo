package vip.yeee.memo.integrate.thirdsdk.aliyun.oss.kit;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.thirdsdk.aliyun.oss.properties.AliOssProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 16:29
 */
@Slf4j
@Component
public class AliyunOssKit {

    @Resource
    private AliOssProperties aliOssProperties;

    private OSS oss;
    private DefaultAcsClient acsClient;

    @PostConstruct
    public void init() {
        this.oss = new OSSClientBuilder()
                .build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
        IClientProfile profile = DefaultProfile.getProfile(aliOssProperties.getRegion(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
        acsClient = new DefaultAcsClient(profile);
        log.info("OSS Client初始化成功：default-bucket = {}", aliOssProperties.getBucketDefault());
    }

    public InputStream getObject(String objectName) {
        return this.getObject(aliOssProperties.getBucketDefault(), objectName);
    }

    public InputStream getObject(String bucketName, String objectName) {
        OSSObject ossObj = oss.getObject(bucketName, handleUrl(objectName));
        return ossObj.getObjectContent();
    }

    public void uploadObjectNoMetadata(InputStream inputStream, String ObjectName) {
        oss.putObject(aliOssProperties.getBucketDefault(), ObjectName, inputStream);
    }

    public void uploadObjectNoMetadata(InputStream inputStream, String bucketName, String ObjectName) {
        oss.putObject(bucketName, ObjectName, inputStream);
    }

    public void uploadObject(InputStream inputStream, String ObjectName) throws IOException {
        this.uploadObject(inputStream, aliOssProperties.getBucketDefault(), ObjectName);
    }

    public void uploadObject(InputStream inputStream, String bucketName, String ObjectName) throws IOException {
        String fileName = ObjectName.substring(ObjectName.lastIndexOf("/") + 1);
        //创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(inputStream.available());
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentType(contentType(fileName.substring(fileName.lastIndexOf("."))));
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + inputStream.available() + "Byte.");
        //上传文件
        oss.putObject(bucketName, ObjectName, inputStream, metadata);
    }

    public void uploadObject(File file, String ObjectName) throws IOException {
        this.uploadObject(file, aliOssProperties.getBucketDefault(), ObjectName);
    }

    public void uploadObject(File file, String bucketName, String ObjectName) throws IOException {
        String fileName = ObjectName.substring(ObjectName.lastIndexOf("/") + 1);
        //创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentType(contentType(fileName.substring(fileName.lastIndexOf("."))));
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + file.length() + "Byte.");
        //上传文件
        oss.putObject(bucketName, ObjectName, file);
    }

    /**
     * STS临时授权
     */
    public AssumeRoleResponse stsAuthorize(String roleRAM, String roleSessionName) throws ClientException {
        AssumeRoleRequest request = new AssumeRoleRequest();
        // 持续秒数 3600秒，即1小时
        request.setDurationSeconds(3600L);
        request.setMethod(MethodType.POST);
        request.setProtocol(ProtocolType.HTTPS);
        request.setRoleArn(roleRAM);
        request.setRoleSessionName(roleSessionName);
        return acsClient.getAcsResponse(request);
    }

    private String contentType(String fileType) {
        fileType = fileType.toLowerCase();
        if ("bmp".equals(fileType)) {
            return "image/bmp";
        } else if ("gif".equals(fileType)) {
            return "image/gif";
        } else if (Arrays.asList("png", "jpeg", "jpg").contains(fileType)) {
            return "image/jpeg";
        } else if ("html".equals(fileType)) {
            return "text/html";
        } else if ("txt".equals(fileType)) {
            return "text/plain";
        } else if ("vsd".equals(fileType)) {
            return "application/vnd.visio";
        } else if (Arrays.asList("ppt", "pptx").contains(fileType)) {
            return "application/vnd.ms-powerpoint";
        } else if (Arrays.asList("doc", "docx").contains(fileType)) {
            return "application/msword";
        } else if ("xml".equals(fileType)) {
            return "text/xml";
        } else if ("mp4".equals(fileType)) {
            return "video/mp4";
        } else {
            return "application/octet-stream";
        }
    }

    private String handleUrl(String url) {
        if (url == null | "".equals(url)) {
            return "";
        }
        while (url.startsWith("/") || url.startsWith("\\")) {
            url = url.substring(1);
        }
        return url;
    }

}
