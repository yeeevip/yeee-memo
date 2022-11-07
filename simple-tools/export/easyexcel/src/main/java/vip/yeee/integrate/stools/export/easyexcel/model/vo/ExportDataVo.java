package vip.yeee.integrate.stools.export.easyexcel.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@HeadStyle(fillPatternType = FillPatternTypeEnum.NO_FILL,
        fillForegroundColor = 1,
        borderLeft = BorderStyleEnum.NONE,
        borderRight = BorderStyleEnum.NONE,
        borderTop = BorderStyleEnum.NONE,
        borderBottom = BorderStyleEnum.NONE)
@HeadFontStyle(fontHeightInPoints = 11, bold = BooleanEnum.FALSE)
@Data
public class ExportDataVo extends BaseExportDataVo {

    @ExcelProperty("订单编号")
    private String id;

    @ExcelProperty("用户ID")
    private String userId;

    @ExcelProperty("下单时间")
    private String createTime;

}
