package com.hr.springboot.multi.tenant.app.filter;

import com.hr.springboot.multi.tenant.app.context.TenantContext;
import com.hr.springboot.multi.tenant.app.security.AuthenticationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("---- Executing Tenant Filter. Trying to extract the tenant information from the token ----");
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantId = AuthenticationService.getTenant(httpRequest);
        TenantContext.setCurrentTenant(tenantId);

        try
        {
            chain.doFilter(request, response);
        }
        finally
        {
            TenantContext.setCurrentTenant("");
        }
    }
}
