package com.zhtangsh.SpringDataMultiTenantMySQL.config;

import javax.annotation.Priority;
import javax.inject.Named;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
@Priority(Priorities.AUTHORIZATION)
@Named
@Provider
public class NameFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String tenantId = requestContext.getHeaderString("X-TENANT-ID");
        EnnContext.setTenantId(tenantId);
    }
}
