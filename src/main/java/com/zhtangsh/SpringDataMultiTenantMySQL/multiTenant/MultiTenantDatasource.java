package com.zhtangsh.SpringDataMultiTenantMySQL.multiTenant;

import com.zhtangsh.SpringDataMultiTenantMySQL.config.EnnContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
public class MultiTenantDatasource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String tenantID = EnnContext.getTenantId();
        return tenantID.equals("") ? "cc" : tenantID;
    }
}
