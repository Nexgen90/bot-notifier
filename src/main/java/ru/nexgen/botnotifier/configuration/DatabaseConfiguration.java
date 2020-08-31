package ru.nexgen.botnotifier.configuration;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.nexgen.botnotifier.configuration.properties.DatabaseProperties;

import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final DatabaseProperties databaseProperties;

    @Autowired
    public DatabaseConfiguration(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();

        dataSource.setDriverClassName(databaseProperties.getDriverClassName());
        dataSource.setUrl(databaseProperties.getUrl());
        dataSource.setUsername(databaseProperties.getUser());
        dataSource.setPassword(databaseProperties.getPassword());
        dataSource.setInitialSize(databaseProperties.getInitialSize());
        dataSource.setMaxActive(databaseProperties.getMaxActive());
        dataSource.setMinIdle(databaseProperties.getMinIdle());
        dataSource.setMaxIdle(databaseProperties.getMaxIdle());
        dataSource.setTestWhileIdle(true);
        dataSource.setJdbcInterceptors(databaseProperties.getJdbcInterceptors());
        dataSource.setDefaultAutoCommit(false);

        try {
            dataSource.getConnection();
        } catch (SQLException exp) {
            throw new IllegalStateException(exp);
        }

        return dataSource;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());

        return dataSourceTransactionManager;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        return sqlSessionFactoryBean;
    }

}
