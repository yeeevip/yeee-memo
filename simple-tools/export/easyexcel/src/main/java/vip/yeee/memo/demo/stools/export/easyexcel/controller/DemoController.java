package vip.yeee.memo.demo.stools.export.easyexcel.controller;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.memo.demo.stools.export.easyexcel.biz.DemoBiz;
import vip.yeee.memo.demo.stools.export.easyexcel.model.vo.ImportDataVo;
import vip.yeee.memo.base.model.rest.CommonResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
public class DemoController {

    @Resource
    private DemoBiz demoBiz;

    @GetMapping("easyexcel/export")
    public void exportData() {
        demoBiz.exportData();
    }

    @PostMapping("easyexcel/import")
    public CommonResult<List<ImportDataVo>> importData(@RequestParam("file") MultipartFile file) {
        return CommonResult.success(demoBiz.importData(file));
    }

    @RequestMapping("/downloadTemplate")
    public ResponseEntity<?> downloadTemplate(HttpServletResponse response) {
        String fileName = new String("导入模板.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        try(InputStream inputStream = this.getClass().getResourceAsStream("/template/template.xlsx")) {
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(IoUtil.readBytes(inputStream), headers, HttpStatus.OK);
        } catch (Exception ex){
            log.error("下载导入模板出现错误",ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
