package com.zhtangsh.SpringDataMultiTenantMySQL.multiTenant;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
@Configuration
public class MultiTenantConfig {

    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource abstractRoutingDataSource = new MultiTenantDatasource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("cc", cc());
        targetDataSources.put("dd", dd());
        abstractRoutingDataSource.setTargetDataSources(targetDataSources);
        abstractRoutingDataSource.afterPropertiesSet();
        return abstractRoutingDataSource;
    }

    private DataSource cc() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://10.19.140.200:32143/cc")
                .username("root")
                .password("root")
                .build();
    }

    private DataSource dd() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://10.19.140.200:32143/dd")
                .username("root")
                .password("root")
                .build();
    }
}
