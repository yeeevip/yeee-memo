package vip.yeee.memo.integrate.custom.generate.service.file;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import vip.yeee.memo.integrate.custom.generate.service.formatter.TemplateFormatter;
import vip.yeee.memo.integrate.custom.generate.service.model.TableClass;

import java.util.Properties;

/**
 * @author liuzh
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

    public GenerateByTemplateFile(TableClass tableClass, TemplateFormatter templateFormatter, Properties properties, String targetProject, String targetPackage, String fileName, String templateContent) {
        super(null, targetProject, ENCODING, null);
        this.targetProject = targetProject;
        this.targetPackage = targetPackage;
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
