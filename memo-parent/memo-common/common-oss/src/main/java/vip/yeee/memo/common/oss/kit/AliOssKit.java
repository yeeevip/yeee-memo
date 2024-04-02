package vip.yeee.memo.common.oss.kit;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;
import vip.yeee.memo.common.oss.model.StsAuthInfo;
import vip.yeee.memo.common.oss.constant.OssConstant;
import vip.yeee.memo.common.oss.properties.AliOssProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 16:29
 */
@Slf4j
@Conditional(AliOssKit.AliOssConditional.class)
@Component
public class AliOssKit implements OssKit {

    @Resource
    private AliOssProperties aliOssProperties;

    private OSS oss;
    private DefaultAcsClient acsClient;

    @PostConstruct
    public void init() {
        this.oss = new OSSClientBuilder()
                .build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKey(), aliOssProperties.getSecretKey());
        IClientProfile profile = DefaultProfile.getProfile(aliOssProperties.getRegion(), aliOssProperties.getAccessKey(), aliOssProperties.getSecretKey());
        acsClient = new DefaultAcsClient(profile);
        log.info("ALI OSS Client初始化成功：default-bucket = {}", aliOssProperties.getDefaultBucket());
    }

    @Override
    public InputStream getObject(String objectName) {
        return this.getObject(aliOssProperties.getDefaultBucket(), objectName);
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) {
        OSSObject ossObj = oss.getObject(bucketName, handleUrl(objectName));
        return ossObj.getObjectContent();
    }

    @Override
    public void uploadObjectNoMetadata(InputStream inputStream, String ObjectName) {
        oss.putObject(aliOssProperties.getDefaultBucket(), ObjectName, inputStream);
    }

    @Override
    public void uploadObjectNoMetadata(InputStream inputStream, String bucketName, String ObjectName) {
        oss.putObject(bucketName, ObjectName, inputStream);
    }

    @Override
    public void uploadObject(InputStream inputStream, String ObjectName) throws IOException {
        this.uploadObject(inputStream, aliOssProperties.getDefaultBucket(), ObjectName);
    }

    @Override
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

    @Override
    public void uploadObject(File file, String ObjectName) throws IOException {
        this.uploadObject(file, aliOssProperties.getDefaultBucket(), ObjectName);
    }

    @Override
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
    @Override
    public StsAuthInfo stsAuthorize(Map<String, Object> params) throws Exception {
        StsAuthInfo authInfo = new StsAuthInfo();
        authInfo.setChannel(OssConstant.Channel.ALI);
        AssumeRoleRequest request = new AssumeRoleRequest();
        // 持续秒数 3600秒，即1小时
        request.setDurationSeconds(3600L);
        request.setMethod(MethodType.POST);
        request.setProtocol(ProtocolType.HTTPS);
        request.setRoleArn(aliOssProperties.getSts().getRoleArn());
        request.setRoleSessionName(aliOssProperties.getSts().getRoleSessionName());
        AssumeRoleResponse acsResponse = acsClient.getAcsResponse(request);
        Map<String, Object> info = new HashMap<>();
        authInfo.setInfo(info);
        return authInfo;
    }

    public static class AliOssConditional implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            try {
                Class.forName("com.aliyun.oss.OSS");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
    }

}
