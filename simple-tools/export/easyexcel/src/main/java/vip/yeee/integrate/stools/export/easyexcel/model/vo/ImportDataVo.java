package vip.yeee.integrate.stools.export.easyexcel.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportDataVo {

    @ExcelProperty(value = "用户名", index = 0)
    private String username;

    @ExcelProperty(value = "问题答案", index = 1)
    private String nameAnswer;

}
