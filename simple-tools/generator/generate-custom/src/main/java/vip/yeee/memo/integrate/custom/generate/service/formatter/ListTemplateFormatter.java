package vip.yeee.memo.integrate.custom.generate.service.formatter;


import vip.yeee.memo.integrate.custom.generate.service.model.TableClass;

import java.util.Properties;
import java.util.Set;

/**
 * @author liuzh
 * @since 3.4.5
 */
public interface ListTemplateFormatter {

    /**
     * 获取根据模板生成的数据
     *
     * @param tableClassSet
     * @param properties
     * @param targetPackage
     * @param templateContent
     * @return
     */
    String getFormattedContent(Set<TableClass> tableClassSet, Properties properties, String targetPackage, String templateContent);
}
