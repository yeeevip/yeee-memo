package vip.yeee.memo.integrate.mp.generate;

import cn.hutool.core.io.resource.ResourceUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * <p>
 * mysql 代码生成器演示例子
 * </p>
 *
 * @author jobob
 * @since 2018-09-12
 */
public class MysqlGenerator {

    // 配置文件
    static Properties properties = null;

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void init() throws IOException {
        properties = new Properties();
        properties.load(ResourceUtil.getStream("C:\\Users\\yeeee\\Desktop\\generator.properties"));
    }

    /**
     * RUN THIS
     */
    public static void main(String[] args) throws IOException {

        // 初始化参数
        init();

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/simple-tools/code-generate-mp/src/main/java");
        gc.setAuthor("yeeee");
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        String datasourceType = properties.getProperty("datasource.type");
        if ("mysql".equalsIgnoreCase(datasourceType)) {
            dsc.setUrl(properties.getProperty("datasource.url"));
            dsc.setDriverName("com.mysql.cj.jdbc.Driver");
            dsc.setUsername(properties.getProperty("datasource.username"));
            dsc.setPassword(properties.getProperty("datasource.password"));
        } else {
            dsc.setUrl(properties.getProperty("datasource.url"));
            dsc.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dsc.setUsername(properties.getProperty("datasource.username"));
            dsc.setPassword(properties.getProperty("datasource.password"));
        }

        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setModuleName(properties.getProperty("module.name"));
        pc.setParent(properties.getProperty("module.parent"));

        // *****************自定义目录*****************
        pc.setMapper("mapper." + pc.getModuleName());
        pc.setService("core.service." + pc.getModuleName());
        pc.setServiceImpl(pc.getService()+".impl");
        pc.setController("web." + pc.getModuleName());
        pc.setEntity("entity." + pc.getModuleName());

        pc.setModuleName("");

        // *****************自定义目录*****************

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/simple-tools/code-generate-mp/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("vip.yeee.memo.integrate.mp.generate.model.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        //strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
        //strategy.setInclude(scanner("表名"));
        strategy.setInclude(properties.getProperty("table.name"));
        //strategy.setSuperEntityColumns("id");
        //strategy.setControllerMappingHyphenStyle(true);
        //strategy.setTablePrefix(pc.getModuleName() + "_");
        strategy.setTablePrefix(properties.getProperty("table.prefix"));
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
