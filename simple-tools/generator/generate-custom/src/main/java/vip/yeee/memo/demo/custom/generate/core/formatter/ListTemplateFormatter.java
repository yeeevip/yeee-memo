package vip.yeee.memo.demo.custom.generate.core.formatter;


import vip.yeee.memo.demo.custom.generate.core.model.TableClass;

import java.util.Properties;
import java.util.Set;

/**
 * @since 3.4.5
 */
public interface ListTemplateFormatter {

    /**
     * 获取根据模板生成的数据
     */
    String getFormattedContent(Set<TableClass> tableClassSet, Properties properties, String targetPackage, String templateContent);
}
