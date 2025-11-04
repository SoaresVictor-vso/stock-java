package com.viso.stock.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.viso.stock.model.Permission;
import com.viso.stock.model.TokenUserEntity;

@Service
public class PermissionService {

    /**
     * Get the current authenticated user from the security context
     * @return TokenUserEntity or null if not authenticated
     */
    public TokenUserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof TokenUserEntity) {
            return (TokenUserEntity) authentication.getPrincipal();
        }
        
        return null;
    }

    /**
     * Check if the current user has a specific permission
     * @param permission The permission to check
     * @return true if the user has the permission, false otherwise
     */
    public boolean hasPermission(Permission permission) {
        TokenUserEntity currentUser = getCurrentUser();
        
        if (currentUser == null) {
            return false;
        }

        return currentUser.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(perm -> perm.getName().equals(permission));
    }

    /**
     * Check if the current user has any of the specified permissions
     * @param permissions The permissions to check
     * @return true if the user has at least one of the permissions, false otherwise
     */
    public boolean hasAnyPermission(Permission... permissions) {
        for (Permission permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the current user has all of the specified permissions
     * @param permissions The permissions to check
     * @return true if the user has all the permissions, false otherwise
     */
    public boolean hasAllPermissions(Permission... permissions) {
        for (Permission permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all permissions for the current user
     * @return Set of permissions or empty set if not authenticated
     */
    public Set<Permission> getCurrentUserPermissions() {
        TokenUserEntity currentUser = getCurrentUser();
        
        if (currentUser == null) {
            return Set.of();
        }

        return currentUser.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(perm -> perm.getName())
                .collect(Collectors.toSet());
    }

    /**
     * Get all role names for the current user
     * @return Set of role names or empty set if not authenticated
     */
    public Set<String> getCurrentUserRoles() {
        TokenUserEntity currentUser = getCurrentUser();
        
        if (currentUser == null) {
            return Set.of();
        }

        return currentUser.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
}