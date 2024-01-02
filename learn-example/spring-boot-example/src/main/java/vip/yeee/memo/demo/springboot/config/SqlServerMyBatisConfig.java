package vip.yeee.memo.demo.springboot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "vip.yeee.memo.demo.springboot.domain.sqlserver.mapper", sqlSessionFactoryRef = "sqlServerSqlSessionFactory", sqlSessionTemplateRef = "sqlServerSqlSessionTemplate")
public class SqlServerMyBatisConfig {

    @Bean(name = "sqlServerDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.sql-server")
    public DataSourceProperties sqlServerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "sqlServerDataSource")
    public DataSource sqlServerDataSource() {
        return sqlServerDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    SqlSessionFactory sqlServerSqlSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(sqlServerDataSource());
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:vip/yeee/memo/demo/springboot/domain/sqlserver/mapper/sqlserver/*.xml"));
            sessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }

    @Bean
    SqlSessionTemplate sqlServerSqlSessionTemplate() {
        return new SqlSessionTemplate(sqlServerSqlSessionFactory());
    }
}
