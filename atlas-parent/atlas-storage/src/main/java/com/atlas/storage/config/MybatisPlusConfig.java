package com.atlas.storage.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class MybatisPlusConfig {

    @Value("${atlas.db.primary.type:mysql}")
    private String primaryDbType;

    @Value("${atlas.db.primary.url:}")
    private String primaryDbUrl;

    @Value("${atlas.db.primary.username:}")
    private String primaryDbUsername;

    @Value("${atlas.db.primary.password:}")
    private String primaryDbPassword;

    @Value("${atlas.db.secondary.path:./data/local.db}")
    private String secondaryDbPath;

    @Bean
    public DataSource dataSource() {
        String dbType = System.getProperty("atlas.db.type", primaryDbType);
        
        if ("sqlite".equalsIgnoreCase(dbType)) {
            log.info("Using SQLite datasource: {}", secondaryDbPath);
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setUrl("jdbc:sqlite:" + secondaryDbPath);
            return dataSource;
        } else {
            log.info("Using MySQL datasource: {}", primaryDbUrl);
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(primaryDbUrl);
            dataSource.setUsername(primaryDbUsername);
            dataSource.setPassword(primaryDbPassword);
            return dataSource;
        }
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
