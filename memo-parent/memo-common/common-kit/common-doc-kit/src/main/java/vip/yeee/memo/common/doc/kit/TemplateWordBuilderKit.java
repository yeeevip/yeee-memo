package vip.yeee.memo.common.doc.kit;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/14 17:22
 */
@Slf4j
public class TemplateWordBuilderKit {

    public static byte[] fillWord(InputStream template, Map<String, String> replaceTxtMap) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = null;
        HWPFDocument doc = null;
        try {
            doc = new HWPFDocument(template);
            Range bodyRange = doc.getRange();
            for (Map.Entry<String, String> entry : replaceTxtMap.entrySet()) {
                bodyRange.replaceText(entry.getKey(), entry.getValue());
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            doc.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("填充word文件异常", e);
            throw e;
        } finally {
            if (template != null) {
                try {
                    template.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
