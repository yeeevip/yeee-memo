package vip.yeee.memo.common.oss.kit;

import vip.yeee.memo.common.oss.model.StsAuthInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/6/12 10:17
 */
public interface OssKit {

    InputStream getObject(String objectName);

    InputStream getObject(String bucketName, String objectName);

    void uploadObjectNoMetadata(InputStream inputStream, String ObjectName);

    void uploadObjectNoMetadata(InputStream inputStream, String bucketName, String ObjectName);

    void uploadObject(InputStream inputStream, String ObjectName) throws IOException;

    void uploadObject(InputStream inputStream, String bucketName, String ObjectName) throws IOException;

    void uploadObject(File file, String ObjectName) throws IOException;

    void uploadObject(File file, String bucketName, String ObjectName) throws IOException;

    /**
     * STS临时授权
     */
    StsAuthInfo stsAuthorize(Map<String, Object> params) throws Exception;

    default String contentType(String fileType) {
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

    default String handleUrl(String url) {
        if (url == null | "".equals(url)) {
            return "";
        }
        while (url.startsWith("/") || url.startsWith("\\")) {
            url = url.substring(1);
        }
        return url;
    }
}
