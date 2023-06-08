package vip.yeee.memo.integrate.custom.generate.core.file;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import vip.yeee.memo.integrate.custom.generate.core.formatter.TemplateFormatter;
import vip.yeee.memo.integrate.custom.generate.core.model.TableClass;

import java.util.Properties;

/**
 * @since 3.4.5
 */
public class GenerateByTemplateFile extends GeneratedJavaFile {
    public static final String ENCODING = "UTF-8";

    private final String targetPackage;

    private final String fileName;

    private final String templateContent;

    private final Properties properties;

    private final TableClass tableClass;

    private final TemplateFormatter templateFormatter;

    public GenerateByTemplateFile(TableClass tableClass, TemplateFormatter templateFormatter, Properties properties, String targetProject, String basePackage, String fileName, String templateContent) {
        super(null, basePackage + "." + properties.get("package"), ENCODING, null);
        properties.put("basePackage", basePackage);
        this.targetPackage = basePackage + "." + properties.get("package");
        this.targetProject = targetProject;
        this.fileName = fileName;
        this.templateContent = templateContent;
        this.properties = properties;
        this.tableClass = tableClass;
        this.templateFormatter = templateFormatter;
    }

    @Override
    public CompilationUnit getCompilationUnit() {
        return null;
    }

    @Override
    public String getFileName() {
        return templateFormatter.getFormattedContent(tableClass, properties, targetPackage, fileName);
    }

    @Override
    public String getFormattedContent() {
        return templateFormatter.getFormattedContent(tableClass, properties, targetPackage, templateContent);
    }

    @Override
    public String getTargetPackage() {
        return templateFormatter.getFormattedContent(tableClass, properties, targetPackage, targetPackage);
    }

    @Override
    public boolean isMergeable() {
        return false;
    }

}
