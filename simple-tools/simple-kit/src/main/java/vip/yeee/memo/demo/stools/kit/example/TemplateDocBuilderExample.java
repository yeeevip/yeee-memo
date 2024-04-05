package vip.yeee.memo.demo.stools.kit.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.google.common.collect.Maps;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfFont;
import com.spire.pdf.graphics.PdfFontFamily;
import com.spire.pdf.graphics.PdfFontStyle;
import com.spire.pdf.graphics.PdfTrueTypeFont;
import com.spire.pdf.graphics.fonts.PdfUsedFont;
import vip.yeee.memo.common.doc.kit.TemplatePdfBuilderKit;
import vip.yeee.memo.common.doc.kit.TemplateWordBuilderKit;

import java.awt.*;
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
//        buildPdf();
//        word2Pdf();

        replaceAllFonts();
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


    public static void replaceAllFonts() throws Exception {
        //创建PdfDocument类的对象
        PdfDocument pdf = new PdfDocument();

        //加载PDF文档
        pdf.loadFromFile("/Users/yeee/Workplaces/private/yeee-memo/simple-tools/simple-kit/files/1_全文片段对照报告.pdf");

        //获取文档中的所有字体
        PdfUsedFont[] fonts = pdf.getUsedFonts();

        //遍历所有字体
        for (PdfUsedFont font: fonts) {

            //获取字体大小
            float fontSize = font.getSize();

            //创建新字体
            PdfFont newfont = new PdfFont(PdfFontFamily.Helvetica, fontSize, PdfFontStyle.Regular);
//            PdfTrueTypeFont newfont = new PdfTrueTypeFont(new Font("宋体", Font.PLAIN, (int) fontSize), true);

            //替换原有字体
            font.replace(newfont);
        }

        //保存文档
        pdf.saveToFile("/Users/yeee/Workplaces/private/yeee-memo/simple-tools/simple-kit/files/ReplaceAllFonts.pdf");
        pdf.dispose();
    }


}
