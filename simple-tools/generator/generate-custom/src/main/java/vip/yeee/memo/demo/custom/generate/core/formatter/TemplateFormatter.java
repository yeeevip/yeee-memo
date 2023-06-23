package vip.yeee.memo.demo.custom.generate.core.formatter;

import vip.yeee.memo.demo.custom.generate.core.model.TableClass;

import java.util.Properties;

/**
 * @since 3.4.5
 */
public interface TemplateFormatter {

    /**
     * 获取根据模板生成的数据
     */
    String getFormattedContent(TableClass tableClass, Properties properties, String targetPackage, String templateContent);
}
