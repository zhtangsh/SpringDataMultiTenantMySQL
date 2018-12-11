package com.zhtangsh.SpringDataMultiTenantMySQL.config;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/10
 */
public class EnnContext {
    private static ThreadLocal<ConcurrentHashMap<String, String>> threadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public static void setTenantId(String tenantId) {
        if (tenantId == null) {
            tenantId = "";
        }
        threadLocal.get().put("tenantId", tenantId);
    }

    public static String getTenantId() {
        return threadLocal.get().getOrDefault("tenantId", "");
    }
}
