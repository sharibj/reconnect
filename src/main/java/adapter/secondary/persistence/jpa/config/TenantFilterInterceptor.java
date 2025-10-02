package adapter.secondary.persistence.jpa.config;

import adapter.primary.http.security.TenantContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TenantFilterInterceptor {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void enableTenantFilter() {
        String currentTenant = TenantContext.getCurrentTenant();
        Session session = entityManager.unwrap(Session.class);

        if (currentTenant != null) {
            session.enableFilter("tenantFilter")
                   .setParameter("username", currentTenant);
        } else {
            // When no tenant context, set filter to match impossible tenant to return empty results
            session.enableFilter("tenantFilter")
                   .setParameter("username", "__NO_TENANT__");
        }
    }

    @Transactional
    public void disableTenantFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter("tenantFilter");
    }
}