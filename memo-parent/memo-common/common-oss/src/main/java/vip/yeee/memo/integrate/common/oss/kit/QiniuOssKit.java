package vip.yeee.memo.integrate.common.oss.kit;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.oss.constant.OssConstant;
import vip.yeee.memo.integrate.common.oss.model.StsAuthInfo;
import vip.yeee.memo.integrate.common.oss.properties.QiniuOssProperties;

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
 * @author yeeee
 * @since 2022/7/12 16:29
 */
@Slf4j
@Conditional(QiniuOssKit.QiniuOssConditional.class)
@Component
public class QiniuOssKit implements OssKit {

    @Resource
    private QiniuOssProperties qiniuOssProperties;

    private Auth auth;

    @PostConstruct
    public void init() {
        String accessKey = qiniuOssProperties.getAccessKey();
        String secretKey = qiniuOssProperties.getSecretKey();
        auth = Auth.create(accessKey, secretKey);
    }

    @Override
    public InputStream getObject(String objectName) {
        return null;
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) {
        return null;
    }

    @Override
    public void uploadObjectNoMetadata(InputStream inputStream, String ObjectName) {

    }

    @Override
    public void uploadObjectNoMetadata(InputStream inputStream, String bucketName, String ObjectName) {

    }

    @Override
    public void uploadObject(InputStream inputStream, String ObjectName) throws IOException {

    }

    @Override
    public void uploadObject(InputStream inputStream, String bucketName, String ObjectName) throws IOException {

    }

    @Override
    public void uploadObject(File file, String ObjectName) throws IOException {

    }

    @Override
    public void uploadObject(File file, String bucketName, String ObjectName) throws IOException {

    }

    @Override
    public StsAuthInfo stsAuthorize(Map<String, Object> params) throws Exception {
        StsAuthInfo authInfo = new StsAuthInfo();
        authInfo.setChannel(OssConstant.Channel.QINIU);

        String fileKey = (String) params.get("fileKey");
        String bucket = (String) params.get("bucket");

        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, fileKey, expireSeconds, putPolicy);

        Map<String, Object> info = new HashMap<>();
        info.put("upToken", upToken);
        authInfo.setInfo(info);
        return authInfo;
    }

    public static class QiniuOssConditional implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            try {
                Class.forName("com.qiniu.util.Auth");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
    }

}
