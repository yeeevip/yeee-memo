package vip.yeee.memo.demo.stools.kit.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.google.common.collect.Maps;
import vip.yeee.memo.common.doc.kit.TemplatePdfBuilderKit;
import vip.yeee.memo.common.doc.kit.TemplateWordBuilderKit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/14 18:05
 */
public class TemplateDocBuilderExample {

    public static void main(String[] args) throws Exception {
//        buildWord();
        buildPdf();
//        word2Pdf();
    }

    private static void buildWord() throws Exception {
        HashMap<String, String> fillMap = Maps.newHashMap();
        fillMap.put("totalMoney", "1000元");
        fillMap.put("startTime", "2023-01-01");
        fillMap.put("endTime", "2023-01-01");
        fillMap.put("firstPayMoney", "100元");
        fillMap.put("firstPayMoneys", "大写100元");
        fillMap.put("surplusMoney", "900元");
        fillMap.put("surplusMoneys", "大写900元");
        fillMap.put("PartyA", "甲公司");
        fillMap.put("PartyB", "乙公司");
        BufferedInputStream template = FileUtil.getInputStream("C:\\Workspaces\\private\\yeee-memo\\memo-parent\\memo-common\\common-kit\\common-doc-kit\\src\\main\\resources\\template\\example.doc");
        byte[] outBytes = TemplateWordBuilderKit.fillWord(template, fillMap);
        BufferedOutputStream filledOS = FileUtil.getOutputStream("C:\\Users\\yeeee\\Desktop\\temp\\test\\filled.doc");
        IoUtil.copy(new ByteArrayInputStream(outBytes), filledOS);
    }

    private static void buildPdf() throws Exception {
        HashMap<String, String> fillMap = Maps.newHashMap();
        fillMap.put("totalMoney", "1000元");
        fillMap.put("startYear", "2023");
        fillMap.put("startMonth", "01");
        fillMap.put("startDay", "01");
        fillMap.put("endYear", "2023");
        fillMap.put("endMonth", "11");
        fillMap.put("endDay", "11");
        fillMap.put("firstPayMoney", "100元");
        fillMap.put("firstPayMoneys", "大写100元");
        fillMap.put("surplusMoney", "900元");
        fillMap.put("surplusMoneys", "大写900元");
        fillMap.put("PartyA", "甲公司");
        fillMap.put("PartyB", "乙公司");
        BufferedInputStream template = FileUtil.getInputStream("C:\\Workspaces\\private\\yeee-memo\\memo-parent\\memo-common\\common-kit\\common-doc-kit\\src\\main\\resources\\template\\example.pdf");
        byte[] outBytes = TemplatePdfBuilderKit.fillPdf(template, fillMap);
        BufferedOutputStream filledOS = FileUtil.getOutputStream("C:\\Users\\yeeee\\Desktop\\temp\\test\\filled.pdf");
        IoUtil.copy(new ByteArrayInputStream(outBytes), filledOS);
    }

    private static void word2Pdf() throws Exception {
        byte[] outBytes = TemplatePdfBuilderKit.word2PdfByte(FileUtil.getInputStream("C:\\Users\\yeeee\\Desktop\\temp\\test\\filled.doc"));
        BufferedOutputStream w2pOS = FileUtil.getOutputStream("C:\\Users\\yeeee\\Desktop\\temp\\test\\word2Pdf.pdf");
        IoUtil.copy(new ByteArrayInputStream(outBytes), w2pOS);
    }
}
