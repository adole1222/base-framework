package com.adole.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @Author: adole
 * @Date: 2019/9/13 22:29
 */
@Configurable("domainDataSource")
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "domain.datasource")
    @Bean("domainDataSource")
    @Qualifier("domainDataSource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }
}
