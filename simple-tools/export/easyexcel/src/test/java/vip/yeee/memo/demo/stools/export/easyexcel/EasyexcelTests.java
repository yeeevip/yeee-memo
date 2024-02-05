package vip.yeee.memo.demo.stools.export.easyexcel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.common.excel.kit.EasyExcelKit;
import vip.yeee.memo.demo.stools.export.easyexcel.model.TestVo;

import java.util.ArrayList;
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
        List<TestVo> testVoList = EasyExcelKit.syncReadExcel(FileUtil.getInputStream("C:\\Users\\yeeee\\Desktop\\新建文件夹\\111\\1111.xlsx"), TestVo.class);
        log.info("size = {}", testVoList.size());
        StringBuilder sb = new StringBuilder("INSERT INTO 11_area (id,pid,rid,`type`,`level`,name,remark) VALUES ");
        for (TestVo vo : testVoList) {
            sb.append("\n").append(StrUtil.format("('{}','{}',NULL,2,2,'{}','{}'),", vo.getId(), vo.getPid(), vo.getName(), vo.getRemark()));
        }
        String finalStr = StrUtil.removeSuffix(sb.toString(), ",") + ";";
        log.info("sql = {}", finalStr);
    }

    // 动态命名列导出
    @Test
    public void exportDataAutoHeadName() {
        ArrayList<String> headList = Lists.newArrayList();
        for (int i = 0; i < 6; i++) {
            headList.add("列名-" + (i + 1));
        }
        ArrayList<List<String>> dataList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            ArrayList<String> columnDatas = Lists.newArrayList();
            for (int j = 0; j < 6; j++) {
                columnDatas.add("行-" + (i + 1) + "#列-" + (j + 1) + "值");
            }
            dataList.add(columnDatas);
        }
        EasyExcelKit.export(FileUtil.getOutputStream("/Users/yeee/log/exportDataAutoHeadName.xlsx"), headList, dataList);
    }

}

