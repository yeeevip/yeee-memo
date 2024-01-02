package vip.yeee.memo.demo.springboot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "vip.yeee.memo.demo.springboot.domain.mysql.mapper", sqlSessionFactoryRef = "mysqlSqlSessionFactory", sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
public class MysqlMyBatisConfig {

    @Bean(name = "mysqlDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource() {
        return mysqlDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    SqlSessionFactory mysqlSqlSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(mysqlDataSource());
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:vip/yeee/memo/demo/springboot/domain/mysql/mapper/sqlserver/*.xml"));
            sessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }

    @Bean
    SqlSessionTemplate mysqlSqlSessionTemplate() {
        return new SqlSessionTemplate(mysqlSqlSessionFactory());
    }


    @Bean
    @ConditionalOnMissingBean(TransactionManager.class)
    DataSourceTransactionManager mysqlTransactionManager(Environment environment) {
        return createTransactionManager(environment, mysqlDataSource());
    }

    private DataSourceTransactionManager createTransactionManager(Environment environment, DataSource dataSource) {
        return environment.getProperty("spring.dao.exceptiontranslation.enabled", Boolean.class, Boolean.TRUE)
                ? new JdbcTransactionManager(dataSource) : new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionTemplate mysqlTransactionTemplate(@Qualifier("mysqlTransactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
