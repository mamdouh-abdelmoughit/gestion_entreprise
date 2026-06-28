package com.btp.security;

import com.btp.entity.Organization;

/**
 * Thread-local storage for the current tenant (organization) context.
 * Used by the TenantInterceptor to set the organization for each request,
 * and by services to filter data by organization.
 */
public class TenantContext {

    private static final ThreadLocal<Long> currentOrganizationId = new ThreadLocal<>();
    private static final ThreadLocal<Organization> currentOrganization = new ThreadLocal<>();

    public static void setCurrentOrganizationId(Long organizationId) {
        currentOrganizationId.set(organizationId);
    }

    public static Long getCurrentOrganizationId() {
        return currentOrganizationId.get();
    }

    public static void setCurrentOrganization(Organization organization) {
        currentOrganization.set(organization);
        if (organization != null) {
            currentOrganizationId.set(organization.getId());
        }
    }

    public static Organization getCurrentOrganization() {
        return currentOrganization.get();
    }

    public static void clear() {
        currentOrganizationId.remove();
        currentOrganization.remove();
    }
}
