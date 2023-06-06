package vip.yeee.integrate.stools.export.easyexcel.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.web.utils.SpringContextUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * description......
 * @author yeeee
 */
@Slf4j
public class EasyExcelUtil {

    public static <T> List<T> syncReadExcel(MultipartFile file, Class<T> clz){
        try {
            return EasyExcel.read(file.getInputStream())
                    .head(clz)
                    .autoCloseStream(Boolean.TRUE)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            log.error("读取文件异常", e);
            throw new BizException("读取文件异常");
        }
    }

    public static <T> List<T> syncReadExcel(InputStream inputStream, Class<T> clz){
        try {
            return EasyExcel.read(inputStream)
                    .head(clz)
                    .autoCloseStream(Boolean.TRUE)
                    .sheet()
                    .doReadSync();
        } catch (Exception e) {
            log.error("读取文件异常", e);
            throw new BizException("读取文件异常");
        }
    }

    public static <T> void export(ExcelWriter excelWriter, WriteSheet writeSheet, List<T> exportDataList) {
        try {
            excelWriter.write(exportDataList, writeSheet);
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> void export(OutputStream out, Class<T> clazz, List<T> exportDataList) {
        try {
            ExcelWriter excelWriter = EasyExcelUtil.buildExcelWriter(out, clazz);
            WriteSheet writeSheet = EasyExcelUtil.buildWriteSheet(excelWriter, 0, "工作表1");
            EasyExcelUtil.export(excelWriter, writeSheet, exportDataList);
            excelWriter.finish();
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> void export2Response(List<T> exportDataList, Class<T> clazz) {
        EasyExcelUtil.export2Response(exportDataList, clazz, null);
    }

    public static <T> void export2Response(List<T> exportDataList, Class<T> clazz, String fileName) {
        try {
            HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            LocalDateTime now = LocalDateTime.now();
            fileName = (StrUtil.isNotBlank(fileName) ? fileName : "export" + "/" + DateUtil.format(now, DatePattern.PURE_DATETIME_MS_PATTERN) + "/" + System.currentTimeMillis()) + ".xlsx";
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ExcelWriter excelWriter = EasyExcelUtil.buildExcelWriter(response.getOutputStream(), clazz);
            WriteSheet writeSheet = EasyExcelUtil.buildWriteSheet(excelWriter, 1, "工作表1");
            EasyExcelUtil.export(excelWriter, writeSheet, exportDataList);
            excelWriter.finish();
        } catch (Exception e) {
            log.error("【导出失败】 ", e);
            throw new BizException("导出失败");
        }
    }

    public static <T> ExcelWriter buildExcelWriter(OutputStream out, Class<T> clazz) {
        return EasyExcelFactory
                .write(out)
                .head(clazz)
                .build();
    }

    public static WriteSheet buildWriteSheet(ExcelWriter excelWriter, Integer sheetNo, String sheetName) {
        return new ExcelWriterSheetBuilder(excelWriter)
                .sheetNo(sheetNo)
                .sheetName(sheetName)
                .build();
    }
}
