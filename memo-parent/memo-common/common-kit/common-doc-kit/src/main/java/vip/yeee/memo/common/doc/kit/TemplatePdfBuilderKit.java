package vip.yeee.memo.common.doc.kit;

import com.documents4j.api.DocumentType;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.documents4j.local.Documents4jLocalServices;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/12/14 17:22
 */
@Slf4j
public class TemplatePdfBuilderKit {

    // 表单填充空白，待解决
    public static byte[] fillPdf(InputStream template, Map<String, String> replaceTxtMap) throws Exception {
        PdfStamper ps = null;
        ByteArrayOutputStream bos = null;
        try {
            PdfReader reader = new PdfReader(template);
            bos = new ByteArrayOutputStream();
            ps = new PdfStamper(reader, bos);
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            ArrayList<BaseFont> fontList = new ArrayList<>();
            fontList.add(bf);
            AcroFields fields = ps.getAcroFields();
            fields.setSubstitutionFonts(fontList);
            for (String key : replaceTxtMap.keySet()) {
                fields.setField(key, replaceTxtMap.get(key));
            }
            ps.setFormFlattening(true);
//            ps.getAcroFields().setGenerateAppearances(true);
            ps.close();
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("填充pdf文件异常", e);
            throw e;
        } finally {
            if (template != null) {
                try {
                    template.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 有问题 com.documents4j.throwables.ConversionInputException: The input file seems to be corrupt
    public  static byte[] word2PdfByte(InputStream word) throws Exception {
        File tempFile = new File(tempFile());
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = word.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            Documents4jLocalServices exporter = new Documents4jLocalServices(20, 25, 60, TimeUnit.SECONDS, 60);
            exporter.export(tempFile, baos, DocumentType.MS_WORD);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("word转换pdf字节流错误！", e);
            throw e;
        } finally {
            if (word != null) {
                word.close();
            }
            if (tempFile.exists() && !tempFile.delete()) {
                log.warn("删除临时文件{}错误！", tempFile.getName());
            }
        }
    }

    private static String tempFile() {
        String fileDir = System.getProperty("java.io.tmpdir") + "/word2pdf/";
        File file = new File(fileDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        return fileDir + UUID.randomUUID() + ".docx";
    }

}
