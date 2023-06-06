package vip.yeee.memo.integrate.custom.generate.core.file;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import vip.yeee.memo.integrate.custom.generate.core.formatter.ListTemplateFormatter;
import vip.yeee.memo.integrate.custom.generate.core.model.TableClass;

import java.util.Properties;
import java.util.Set;

/**
 * @since 3.4.5
 */
public class GenerateByListTemplateFile extends GeneratedJavaFile {
    public static final String ENCODING = "UTF-8";

    private final String targetPackage;

    private final String fileNameTemplate;

    private final String templateContent;

    private final Properties properties;

    private final Set<TableClass> tableClassSet;

    private final ListTemplateFormatter templateFormatter;

    public GenerateByListTemplateFile(Set<TableClass> tableClassSet, ListTemplateFormatter templateFormatter, Properties properties, String targetProject, String targetPackage, String fileNameTemplate, String templateContent) {
        super(null, targetProject, ENCODING, null);
        this.targetProject = targetProject;
        this.targetPackage = targetPackage;
        this.fileNameTemplate = fileNameTemplate;
        this.templateContent = templateContent;
        this.properties = properties;
        this.tableClassSet = tableClassSet;
        this.templateFormatter = templateFormatter;
    }

    @Override
    public CompilationUnit getCompilationUnit() {
        return null;
    }

    @Override
    public String getFileName() {
        return templateFormatter.getFormattedContent(tableClassSet, properties, targetPackage, fileNameTemplate);
    }

    @Override
    public String getFormattedContent() {
        return templateFormatter.getFormattedContent(tableClassSet, properties, targetPackage, templateContent);
    }

    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public boolean isMergeable() {
        return false;
    }

}
