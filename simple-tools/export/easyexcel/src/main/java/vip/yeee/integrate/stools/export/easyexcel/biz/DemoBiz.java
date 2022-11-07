package vip.yeee.integrate.stools.export.easyexcel.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vip.yeee.integrate.stools.export.easyexcel.model.vo.ExportDataVo;
import vip.yeee.integrate.stools.export.easyexcel.model.vo.ImportDataVo;
import vip.yeee.integrate.stools.export.easyexcel.utils.EasyExcelUtil;

import java.util.List;

@Component
public class DemoBiz {

    public void exportData() {
        List<ExportDataVo> exportDataVoList = Lists.newArrayList();
        EasyExcelUtil.export2Response(exportDataVoList);
    }

    public List<ImportDataVo> importData(MultipartFile file) {
        List<ImportDataVo> voList = EasyExcelUtil.syncReadExcel(file, ImportDataVo.class);
        return voList;
    }
}
