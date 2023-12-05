package vip.yeee.memo.demo.custom.generate.core.formatter;


import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import vip.yeee.memo.demo.custom.generate.core.model.TableClass;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基于 freemarker 的实现
 *
 * @since 3.4.5
 */
public class FreemarkerTemplateFormatter implements TemplateFormatter, ListTemplateFormatter {
    private final Configuration        configuration  = new Configuration(Configuration.VERSION_2_3_23);

    public FreemarkerTemplateFormatter() {
        configuration.setLocale(Locale.CHINA);
        configuration.setDefaultEncoding("UTF-8");
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
    }

    /**
     * 根据模板处理
     */
    public String process(String templateName, String templateSource, Map<String, Object> params) {
        try {
            Template template = new Template(templateName, templateSource, configuration);
            Writer writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormattedContent(TableClass tableClass, Properties properties, String targetPackage, String templateContent) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Object o : properties.keySet()) {
            params.put(String.valueOf(o), properties.get(o));
        }
        params.put("props", properties);
        params.put("package", targetPackage);
        params.put("tableClass", tableClass);
        params.put("generateDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));//生成时间
        return process(properties.getProperty("templatePath"), templateContent, params);
    }

    @Override
    public String getFormattedContent(Set<TableClass> tableClassSet, Properties properties, String targetPackage, String templateContent) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Object o : properties.keySet()) {
            params.put(String.valueOf(o), properties.get(o));
        }
        params.put("props", properties);
        params.put("package", targetPackage);
        params.put("tableClassSet", tableClassSet);
        return process(properties.getProperty("templatePath"), templateContent, params);
    }
}
