package vip.yeee.memo.integrate.custom.generate.service.formatter;

import vip.yeee.memo.integrate.custom.generate.service.model.TableClass;

import java.util.Properties;

/**
 * @author liuzh
 * @since 3.4.5
 */
public interface TemplateFormatter {

    /**
     * 获取根据模板生成的数据
     *
     * @param tableClass
     * @param properties
     * @param targetPackage
     * @param templateContent
     * @return
     */
    String getFormattedContent(TableClass tableClass, Properties properties, String targetPackage, String templateContent);
}
