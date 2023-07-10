package vip.yeee.memo.demo.stools.export.easyexcel.biz;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.memo.common.excel.kit.EasyExcelKit;
import vip.yeee.memo.demo.stools.export.easyexcel.model.vo.ExportDataVo;
import vip.yeee.memo.demo.stools.export.easyexcel.model.vo.ImportDataVo;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DemoBiz {

    public void exportData() {
        List<ExportDataVo> exportDataVoList = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            ExportDataVo exportDataVo = new ExportDataVo();
            exportDataVo.setId("100000-" + i + 1);
            exportDataVo.setUserId("20000000-" + i + 1);
            exportDataVo.setCreateTime(LocalDateTime.now());
            exportDataVoList.add(exportDataVo);
        }
        EasyExcelKit.export2Response(exportDataVoList, ExportDataVo.class);
    }

    public List<ImportDataVo> importData(MultipartFile file) {
        List<ImportDataVo> voList = EasyExcelKit.syncReadExcel(file, ImportDataVo.class);
        return voList;
    }
}
