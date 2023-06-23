package vip.yeee.memo.demo.stools.export.easyexcel.model.vo;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

//@ExcelIgnoreUnannotated
//@HeadStyle(fillPatternType = FillPatternTypeEnum.NO_FILL,
//        fillForegroundColor = 1,
//        borderLeft = BorderStyleEnum.NONE,
//        borderRight = BorderStyleEnum.NONE,
//        borderTop = BorderStyleEnum.NONE,
//        borderBottom = BorderStyleEnum.NONE)
@HeadFontStyle(fontHeightInPoints = 11, bold = BooleanEnum.FALSE)
@Data
public class ExportDataVo {

    @ExcelProperty("订单编号")
    private String id;

    @ExcelProperty("用户ID")
    private String userId;

    @ExcelProperty("下单时间")
    @ColumnWidth(20)
    @DateTimeFormat(DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;

}
