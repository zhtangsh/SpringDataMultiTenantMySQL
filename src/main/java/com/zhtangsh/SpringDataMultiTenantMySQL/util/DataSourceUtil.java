package com.zhtangsh.SpringDataMultiTenantMySQL.util;

import com.zaxxer.hikari.HikariDataSource;
import com.zhtangsh.SpringDataMultiTenantMySQL.master.model.MasterTenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
public final class DataSourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);

    public static DataSource createConfiguredDataSource(MasterTenant masterTenant) {
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(masterTenant.getUsername());
        ds.setPassword(masterTenant.getPassword());
        ds.setJdbcUrl(masterTenant.getUrl());
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setConnectionTestQuery("SELECT 1");
        ds.setConnectionTimeout(20000);
        ds.setMinimumIdle(5);
        ds.setMaximumPoolSize(10);
        ds.setIdleTimeout(300000);
        ds.setPoolName(masterTenant.getTenantId() + "-connection-pool");
        logger.info("Configured datasource: {}");
        return ds;
    }
}
