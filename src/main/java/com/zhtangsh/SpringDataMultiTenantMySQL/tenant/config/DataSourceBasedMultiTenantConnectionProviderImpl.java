package com.zhtangsh.SpringDataMultiTenantMySQL.tenant.config;

import com.zhtangsh.SpringDataMultiTenantMySQL.config.EnnContext;
import com.zhtangsh.SpringDataMultiTenantMySQL.master.model.MasterTenant;
import com.zhtangsh.SpringDataMultiTenantMySQL.master.repository.MasterTenantRepository;
import com.zhtangsh.SpringDataMultiTenantMySQL.util.DataSourceUtil;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
@Configuration
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);
    private static final long serialVersionUID = 1L;

    @Autowired
    private MasterTenantRepository masterTenantRepository;
    private Map<String, DataSource> dataSourceMap = new HashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        if (dataSourceMap.isEmpty()) {
            List<MasterTenant> masterTenants = masterTenantRepository.findAll();
            logger.info("selectAnyDataSource() --  total tenants: {}", masterTenants.size());
            for (MasterTenant masterTenant : masterTenants) {
                DataSource dataSource = DataSourceUtil.createConfiguredDataSource(masterTenant);
                genSchemas(dataSource);
                dataSourceMap.put(masterTenant.getTenantId(), dataSource);
            }
        }
        return this.dataSourceMap.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        tenantIdentifier = EnnContext.getTenantId();
        if (!this.dataSourceMap.containsKey(tenantIdentifier)) {
            List<MasterTenant> masterTenants = masterTenantRepository.findAll();
            logger.info("selectDataSource() -- total tenants:" + masterTenants.size());
            for (MasterTenant masterTenant : masterTenants) {
                DataSource dataSource = DataSourceUtil.createConfiguredDataSource(masterTenant);
                genSchemas(dataSource);
                this.dataSourceMap.put(masterTenant.getTenantId(), dataSource);
            }
        }
        return this.dataSourceMap.get(tenantIdentifier);
    }


    private void genSchemas(DataSource dataSource) {
        logger.info("Start the configuration of entityManagerFactory for datasource {}", dataSource);
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan(
                new String[]{
                        "com.zhtangsh.SpringDataMultiTenantMySQL.tenant.model",
                        "com.zhtangsh.SpringDataMultiTenantMySQL.tenant.repository"
                }
        );
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.setPersistenceUnitName(dataSource.toString());
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        emfBean.setJpaProperties(properties);
        logger.info("End pf entityManagerFactory {} setup", dataSource);
        emfBean.afterPropertiesSet();
        emfBean.destroy();
    }
}
