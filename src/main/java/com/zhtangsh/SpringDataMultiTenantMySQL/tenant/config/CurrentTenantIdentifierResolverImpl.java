package com.zhtangsh.SpringDataMultiTenantMySQL.tenant.config;

import com.zhtangsh.SpringDataMultiTenantMySQL.config.EnnContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        return EnnContext.getTenantId();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
