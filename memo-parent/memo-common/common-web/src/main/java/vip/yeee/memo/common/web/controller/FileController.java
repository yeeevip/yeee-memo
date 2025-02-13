package vip.yeee.memo.common.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 14:02
 */
@RestController
@RequestMapping("files")
public class FileController {

    @GetMapping("/download/{filePath}")
    public void download(@PathVariable("filePath") String filePath, HttpServletResponse response
            , @RequestHeader(value = "Range", required = false) String range) throws Exception {

        // inline支持浏览器直接打开，比如mp3， attachment支持下载
        response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(filePath, "UTF-8"));
        File file = FileUtil.file(filePath);
        if (StrUtil.isNotEmpty(range)) { // 对于音视频的播放，支持拖动进度需要返回
            // 获取文件的总大小
            long fileSize = file.length();
            // 解析 Range 头
            String[] ranges = range.replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = (ranges.length > 1 && !ranges[1].isEmpty()) ? Long.parseLong(ranges[1]) : fileSize - 1;

            // 限制 end 值不超出文件的实际大小
            end = Math.min(end, fileSize - 1);

            // 计算文件的分块大小
            long chunkSize = end - start + 1;
            response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(chunkSize));

            response.addHeader("Accept-Ranges","bytes");

        }
        IoUtil.copy(FileUtil.getInputStream(file), response.getOutputStream());
    }

}
