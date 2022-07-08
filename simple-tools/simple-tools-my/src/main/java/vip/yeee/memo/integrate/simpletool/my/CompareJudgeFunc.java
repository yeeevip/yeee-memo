package vip.yeee.memo.integrate.simpletool.my;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * @Description:
 * @Author: yeeeeeeee
 * @Date: 2021/12/6 14:54
 */
public class CompareJudgeFunc {

    /**
     * Date compare , YYYY.MM
     */
    private static int compareFunc(String time1, String time2) {
        if(StrUtil.length(time1) < 7 || !NumberUtil.isNumber(StrUtil.sub(time1, 0, 1))) return 1;
        if(StrUtil.length(time2) < 7 || !NumberUtil.isNumber(StrUtil.sub(time2, 0, 1))) return -1;
        return DateUtil.parse(StrUtil.sub(time1, 0, 4) + "-" + StrUtil.sub(time1, 5, 7), "yyyy-MM")
                .compareTo(DateUtil.parse(StrUtil.sub(time2, 0, 4) + "-" + StrUtil.sub(time2, 5, 7), "yyyy-MM")) * (-1);
    }

    /**
     * dateRange1：[2019.02-2020.01，2020.02-2020.12]
     * dateRange2：[2019.01-2020.01，2020.02-2020.12]
     * gapGt：> x 月
     * gapLe：<= y 月
     */
    public static boolean judgeWorkDateDiff(String[] dateRange1, String[] dateRange2, Integer gapGt, Integer gapLe) {
        if(ArrayUtil.isEmpty(dateRange1) || ArrayUtil.isEmpty(dateRange2) || dateRange1.length != dateRange2.length) {
            return false;
        }
        boolean flag = true;
        gapGt = gapGt == null ? Integer.MIN_VALUE : gapGt;
        gapLe = gapLe == null ? Integer.MAX_VALUE : gapLe;
        long gap = 0;
        for(int i= 0; i < dateRange1.length; i++) {
            List<String> dateRange1Arr = StrUtil.splitTrim(dateRange1[i], "-");
            List<String> dateRange2Arr = StrUtil.splitTrim(dateRange2[i], "-");
            if(dateRange1Arr.size() != 2 || dateRange2Arr.size() != 2) continue;
            gap += (Math.abs(DateUtil.parse(dateRange1Arr.get(0), "yyyy.MM").getTime() - DateUtil.parse(dateRange2Arr.get(0), "yyyy.MM").getTime())
                    + Math.abs(DateUtil.parse(dateRange1Arr.get(1), "yyyy.MM").getTime() - DateUtil.parse(dateRange2Arr.get(1), "yyyy.MM").getTime())) / 1000 /60 /60 / 24;
            flag = false;
        }

        return !flag && gap > gapGt && (gap < gapLe  || gap == gapLe);
    }

    /**
     * @Author: yeee
     * @Date: 2021/6/28 15:37
     */
    public static boolean judgeSalaryDiff(String salaryRange1, String salaryRange2, Integer gapGt, Integer gapLe) {
        if(StrUtil.isBlank(salaryRange1) || StrUtil.isBlank(salaryRange2)) {
            return false;
        }
        gapGt = gapGt == null ? Integer.MIN_VALUE : gapGt;
        gapLe = gapLe == null ? Integer.MAX_VALUE : gapLe;
        float s1, s2, gap;
        gap = Math.abs(Float.parseFloat(salaryRange1) - (s2 = Float.parseFloat(salaryRange2))) / s2 * 100;
        return gap > gapGt && (gap == gapLe || gap < gapLe);
    }

}
