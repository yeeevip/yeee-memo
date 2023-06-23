package vip.yeee.memo.demo.stools.export.easyexcel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.memo.demo.stools.export.easyexcel.biz.DemoBiz;
import vip.yeee.memo.demo.stools.export.easyexcel.model.vo.ImportDataVo;
import vip.yeee.memo.base.model.rest.CommonResult;

import javax.annotation.Resource;
import java.util.List;

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

}
