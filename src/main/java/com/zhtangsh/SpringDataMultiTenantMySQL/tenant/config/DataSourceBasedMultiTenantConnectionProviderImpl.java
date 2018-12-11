package com.zhtangsh.SpringDataMultiTenantMySQL.tenant.config;

import com.zhtangsh.SpringDataMultiTenantMySQL.config.EnnContext;
import com.zhtangsh.SpringDataMultiTenantMySQL.master.model.MasterTenant;
import com.zhtangsh.SpringDataMultiTenantMySQL.master.repository.MasterTenantRepository;
import com.zhtangsh.SpringDataMultiTenantMySQL.util.DataSourceUtil;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                dataSourceMap.put(masterTenant.getTenantId(), DataSourceUtil.createConfiguredDataSource(masterTenant));
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
                this.dataSourceMap.put(masterTenant.getTenantId(), DataSourceUtil.createConfiguredDataSource(masterTenant));
            }
        }
        return this.dataSourceMap.get(tenantIdentifier);
    }
}
