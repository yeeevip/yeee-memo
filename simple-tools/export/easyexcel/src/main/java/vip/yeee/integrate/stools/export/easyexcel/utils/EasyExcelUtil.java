package vip.yeee.integrate.stools.export.easyexcel.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.integrate.stools.export.easyexcel.model.vo.BaseExportDataVo;
import vip.yeee.integrate.stools.export.easyexcel.model.vo.ExportDataVo;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.common.web.utils.SpringContextUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
            log.error("获取输入流异常", e);
            throw new BizException("获取输入流异常");
        }
    }

    public static void export2Response(List<? extends BaseExportDataVo> exportDataVoList) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            EasyExcel.write(out, BaseExportDataVo.class).sheet().doWrite(exportDataVoList);
            HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            LocalDateTime now = LocalDateTime.now();
            String fileName = "export" + "/" + DateUtil.format(now, DatePattern.PURE_DATETIME_MS_PATTERN) + "/" + System.currentTimeMillis() + ".xlsx";
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IoUtil.copy(new ByteArrayInputStream(out.toByteArray()), response.getOutputStream());
        } catch (Exception e) {
            log.error("【导出订单失败】 ", e);
            throw new BizException("导出失败");
        }
    }
}
