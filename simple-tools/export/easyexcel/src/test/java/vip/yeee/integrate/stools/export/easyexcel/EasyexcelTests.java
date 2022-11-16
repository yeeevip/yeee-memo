package vip.yeee.integrate.stools.export.easyexcel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.integrate.stools.export.easyexcel.model.TestVo;
import vip.yeee.integrate.stools.export.easyexcel.utils.EasyExcelUtil;

import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/22 13:59
 */
@Slf4j
@SpringBootTest
public class EasyexcelTests {

    @Test
    public void importData1() {
        List<TestVo> testVoList = EasyExcelUtil.syncReadExcel(FileUtil.getInputStream("C:\\Users\\yeeee\\Desktop\\新建文件夹\\111\\1111.xlsx"), TestVo.class);
        log.info("size = {}", testVoList.size());
        StringBuilder sb = new StringBuilder("INSERT INTO 11_area (id,pid,rid,`type`,`level`,name,remark) VALUES ");
        for (TestVo vo : testVoList) {
            sb.append("\n").append(StrUtil.format("('{}','{}',NULL,2,2,'{}','{}'),", vo.getId(), vo.getPid(), vo.getName(), vo.getRemark()));
        }
        String finalStr = StrUtil.removeSuffix(sb.toString(), ",") + ";";
        log.info("sql = {}", finalStr);
    }

}

