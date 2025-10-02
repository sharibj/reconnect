package adapter.secondary.persistence.jpa.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {

    @Autowired
    private TenantFilterInterceptor tenantFilterInterceptor;

    @Before("execution(* adapter.secondary.persistence.jpa.repository.*.*(..))")
    public void enableTenantFilter() {
        tenantFilterInterceptor.enableTenantFilter();
    }
}