package vip.yeee.integrate.stools.export.easyexcel.biz;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.integrate.stools.export.easyexcel.model.vo.ExportDataVo;
import vip.yeee.integrate.stools.export.easyexcel.model.vo.ImportDataVo;
import vip.yeee.integrate.stools.export.easyexcel.utils.EasyExcelUtil;

import java.util.Date;
import java.util.List;

@Component
public class DemoBiz {

    public void exportData() {
        List<ExportDataVo> exportDataVoList = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            ExportDataVo exportDataVo = new ExportDataVo();
            exportDataVo.setId("100000-" + i + 1);
            exportDataVo.setUserId("20000000-" + i + 1);
            exportDataVo.setCreateTime(DateUtil.formatTime(new Date()));
            exportDataVoList.add(exportDataVo);
        }
        EasyExcelUtil.export2Response(exportDataVoList, ExportDataVo.class);
    }

    public List<ImportDataVo> importData(MultipartFile file) {
        List<ImportDataVo> voList = EasyExcelUtil.syncReadExcel(file, ImportDataVo.class);
        return voList;
    }
}
