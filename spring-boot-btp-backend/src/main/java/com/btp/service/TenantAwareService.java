package com.btp.service;

import com.btp.entity.Organization;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.exception.UnauthorizedException;
import com.btp.repository.UserRepository;
import com.btp.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Base service providing multi-tenancy (organization) context helpers.
 * Other services can use this to get the current user's organization
 * and filter data appropriately.
 */
@Service
@RequiredArgsConstructor
public class TenantAwareService {

    private final UserRepository userRepository;

    /**
     * Get the currently authenticated user from the security context.
     */
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user '" + username + "' not found"));
    }

    /**
     * Get the organization of the currently authenticated user.
     * For sub-accounts created by an admin, we need to trace back to the admin's organization.
     */
    public Organization getCurrentOrganization() {
        // First check if TenantContext has been set by the interceptor
        Organization contextOrg = TenantContext.getCurrentOrganization();
        if (contextOrg != null) {
            return contextOrg;
        }

        // Otherwise, get it from the current user
        User currentUser = getCurrentUser();
        return getOrganizationForUser(currentUser);
    }

    /**
     * Get the organization ID for the current user.
     * Returns null if user has no organization (e.g., first-time admin before setup).
     */
    public Long getCurrentOrganizationId() {
        Organization org = getCurrentOrganization();
        return org != null ? org.getId() : null;
    }

    /**
     * Get the organization for a specific user.
     * If the user has a direct organization, use that.
     * If not, but they have a createdByAdmin, use the admin's organization.
     */
    public Organization getOrganizationForUser(User user) {
        // Direct organization link
        if (user.getOrganization() != null) {
            return user.getOrganization();
        }

        // For sub-accounts, get the admin's organization
        if (user.getCreatedByAdmin() != null) {
            return getOrganizationForUser(user.getCreatedByAdmin());
        }

        // No organization found
        return null;
    }

    /**
     * Verify that the current user has access to the specified organization.
     * Throws UnauthorizedException if not.
     */
    public void verifyOrganizationAccess(Long organizationId) {
        Long currentOrgId = getCurrentOrganizationId();
        if (currentOrgId != null && !currentOrgId.equals(organizationId)) {
            throw new UnauthorizedException("Access denied: You do not have permission to access this organization's data");
        }
    }

    /**
     * Check if the current user is an admin.
     */
    public boolean isCurrentUserAdmin() {
        User user = getCurrentUser();
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getNom()));
    }

    /**
     * Check if the current user has a specific role.
     */
    public boolean hasRole(String roleName) {
        User user = getCurrentUser();
        return user.getRoles().stream()
                .anyMatch(role -> roleName.equals(role.getNom()));
    }
}
