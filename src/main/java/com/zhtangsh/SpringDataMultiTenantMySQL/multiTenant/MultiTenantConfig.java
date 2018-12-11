package com.zhtangsh.SpringDataMultiTenantMySQL.multiTenant;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
@Configuration
public class MultiTenantConfig {
    private static final Logger logger = LoggerFactory.getLogger(MultiTenantConfig.class);

    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource abstractRoutingDataSource = new MultiTenantDatasource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource cc = cc();
        DataSource dd = dd();
        genSchemas(cc);
        genSchemas(dd);
        targetDataSources.put("cc", cc);
        targetDataSources.put("dd", dd);
        abstractRoutingDataSource.setTargetDataSources(targetDataSources);
        abstractRoutingDataSource.afterPropertiesSet();
        return abstractRoutingDataSource;
    }

    private DataSource cc() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://10.19.140.200:32143/ee?createDatabaseIfNotExist=true")
                .username("root")
                .password("root")
                .build();
    }

    private DataSource dd() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://10.19.140.200:32143/ff?createDatabaseIfNotExist=true")
                .username("root")
                .password("root")
                .build();
    }

    private void genSchemas(DataSource dataSource) {
        String uid = UUID.randomUUID().toString();
        logger.info("Start the configuration of entityManagerFactory for datasource {}", uid);
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan(
                new String[]{
                        "com.zhtangsh.SpringDataMultiTenantMySQL.data"
                }
        );
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.setPersistenceUnitName(uid);
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.SHOW_SQL, false);
        properties.put(Environment.FORMAT_SQL, false);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        emfBean.setJpaProperties(properties);
        logger.info("End pf entityManagerFactory {} setup", uid);
        emfBean.afterPropertiesSet();
        emfBean.destroy();
    }
}
