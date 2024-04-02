package vip.yeee.memo.demo.stools.export.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/22 14:12
 */
@Data
public class TestVo {
    @ExcelProperty(value = "单位编码")
    private String id;

    @ExcelProperty(value = "区域编码")
    private String pid;

    @ExcelProperty(value = "单位名称")
    private String name;

    @ExcelProperty(value = "备注")
    private String remark;
}
