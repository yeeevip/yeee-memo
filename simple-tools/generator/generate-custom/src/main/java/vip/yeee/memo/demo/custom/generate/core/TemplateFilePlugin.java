package vip.yeee.memo.demo.custom.generate.core;

import cn.hutool.core.util.StrUtil;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;
import vip.yeee.memo.demo.custom.generate.core.model.TableClass;
import vip.yeee.memo.demo.custom.generate.core.file.GenerateByListTemplateFile;
import vip.yeee.memo.demo.custom.generate.core.file.GenerateByTemplateFile;
import vip.yeee.memo.demo.custom.generate.core.formatter.ListTemplateFormatter;
import vip.yeee.memo.demo.custom.generate.core.formatter.TemplateFormatter;
import vip.yeee.memo.demo.custom.generate.core.model.TableColumnBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * 每一个模板都需要配置一个插件，可以配置多个
 * <p>
 * <pre>
 * &lt;plugin type="xxx.TemplateFilePlugin"&gt;
 *      &lt;property name="targetProject"     value="src/main/java"/&gt;
 *      &lt;property name="targetPackage"     value="com.xxx.controller"/&gt;
 *      &lt;property name="templatePath"      value="template/controller.ftl"/&gt;
 *      &lt;property name="fileName"          value="XXXController.java"/&gt;
 *      &lt;property name="templateFormatter" value="xxx.FreemarkerTemplateFormatter"/&gt;
 * &lt;/plugin&gt;
 * </pre>
 * @since 3.4.5
 */
public class TemplateFilePlugin extends PluginAdapter {
    /**
     * 默认的模板格式化类
     */
    public static final String DEFAULT_TEMPLATEFORMATTER = "vip.yeee.memo.demo.custom.generate.core.formatter.FreemarkerTemplateFormatter";
    /**
     * 单个文件模式
     */
    private String          singleMode;
    /**
     * 项目路径（目录需要已经存在）
     */
    private String          targetDir;
    /**
     * 生成的包（路径不存在则创建）
     */
    private String          basePackage;
    /**
     * 模板路径
     */
    private String          templatePath;
    /**
     * 模板内容
     */
    private String          templateContent;
    /**
     * 文件名模板，通过模板方式生成文件名，包含后缀
     */
    private String          fileName;
    /**
     * 模板生成器
     */
    private Object          templateFormatter;
    private String          templateFormatterClass;
    private Set<TableClass> cacheTables;
    
    /**
     * 编码
     */
    private String encoding;

    /**
     * 读取文件
     */
    protected String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        StringBuilder stringBuffer = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            stringBuffer.append(line).append("\n");
            line = reader.readLine();
        }
        return stringBuffer.toString();
    }

    @Override
    public boolean validate(List<String> warnings) {
        boolean right = true;
        if (!StringUtility.stringHasValue(fileName)) {
            warnings.add("没有配置 \"fileName\" 文件名模板，因此不会生成任何额外代码!");
            right = false;
        }
        if (!StringUtility.stringHasValue(templatePath)) {
            warnings.add("没有配置 \"templatePath\" 模板路径，因此不会生成任何额外代码!");
            right = false;
        } else {
            try {
                URL resourceUrl = null;
                try {
                    resourceUrl = ObjectFactory.getResource(templatePath);
                } catch (Exception e) {
                    warnings.add("本地加载\"templatePath\" 模板路径失败，尝试 URL 方式获取!");
                }
                if (resourceUrl == null) {
                    resourceUrl = new URL(templatePath);
                }
                InputStream inputStream = resourceUrl.openConnection().getInputStream();
                templateContent = read(inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                warnings.add("读取模板文件出错: " + e.getMessage());
                right = false;
            }
        }
        if (!StringUtility.stringHasValue(templateFormatterClass)) {
            templateFormatterClass = DEFAULT_TEMPLATEFORMATTER;
            warnings.add("没有配置 \"templateFormatterClass\" 模板处理器，使用默认的处理器!");
        }
        try {
            templateFormatter = Class.forName(templateFormatterClass).newInstance();
        } catch (Exception e) {
            warnings.add("初始化 templateFormatter 出错:" + e.getMessage());
            right = false;
        }
        if (!right) {
            return false;
        }
        int errorCount = 0;
        if (!StringUtility.stringHasValue(targetDir)) {
            errorCount++;
            warnings.add("没有配置 \"targetProject\" 路径!");
        }
        if (!StringUtility.stringHasValue(targetDir)) {
            errorCount++;
            warnings.add("没有配置 \"targetPackage\" 路径!");
        }
        if (errorCount >= 2) {
            warnings.add("由于没有配置任何有效路径，不会生成任何额外代码!");
            return false;
        }
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> list = new ArrayList<GeneratedJavaFile>();
        TableClass tableClass = TableColumnBuilder.build(introspectedTable);
        if ("TRUE".equalsIgnoreCase(singleMode)) {
            properties.put("genType", this.context.getProperty("genType"));
            list.add(new GenerateByTemplateFile(tableClass, (TemplateFormatter) templateFormatter, properties, targetDir, basePackage, fileName, templateContent));
        } else {
            cacheTables.add(tableClass);
        }
        return list;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        List<GeneratedJavaFile> list = new ArrayList<GeneratedJavaFile>();
        if (cacheTables != null && cacheTables.size() > 0) {
            list.add(new GenerateByListTemplateFile(cacheTables, (ListTemplateFormatter) templateFormatter, properties, targetDir, basePackage, fileName, templateContent));
        }
        return list;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.singleMode = properties.getProperty("singleMode", "true");
        if (!"TRUE".equalsIgnoreCase(singleMode)) {
            this.cacheTables = new LinkedHashSet<TableClass>();
        }
        this.targetDir = StrUtil.emptyToDefault(properties.getProperty("targetDir"), this.context.getProperty("targetDir"));
        this.basePackage = StrUtil.emptyToDefault(properties.getProperty("basePackage"), this.context.getProperty("basePackage"));
        this.templatePath = properties.getProperty("templatePath");
        this.fileName = properties.getProperty("fileName");
        this.templateFormatterClass = properties.getProperty("templateFormatter");
        this.encoding = properties.getProperty("encoding", "UTF-8");
    }
}
