package ru.nexgen.botnotifier.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties("db.postgres")
public class DatabaseProperties {

    private String driverClassName = "org.postgresql.Driver";

    @Value("${db.url}")
    private String url;
    @Value("${db.user}")
    private String user;
    @Value("${db.password}")
    private String password;
    @Value("${db.initialSize:20}")
    private Integer initialSize;
    @Value("${db.maxActive:200}")
    private Integer maxActive;
    @Value("${db.minIdle:5}")
    private Integer minIdle;
    @Value("${db.maxIdle:50}")
    private Integer maxIdle;
    private String jdbcInterceptors = "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer";

}
