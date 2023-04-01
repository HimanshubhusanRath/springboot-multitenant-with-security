package com.hr.springboot.multi.tenant.app.context;

public class TenantContext {
    private static final ThreadLocal<String> CURR_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant()
    {
        return CURR_TENANT.get();
    }

    public static void setCurrentTenant(final String tenant)
    {
        CURR_TENANT.set(tenant);
    }
}
