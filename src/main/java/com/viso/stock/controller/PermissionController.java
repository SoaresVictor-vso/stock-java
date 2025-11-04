package com.viso.stock.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viso.stock.model.Permission;
import com.viso.stock.model.TokenUserEntity;
import com.viso.stock.service.PermissionService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Get current user information and permissions
     */
    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUserInfo() {
        TokenUserEntity currentUser = permissionService.getCurrentUser();
        
        if (currentUser == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("authenticated", true);
        userInfo.put("userId", currentUser.getId());
        userInfo.put("name", currentUser.getName());
        userInfo.put("roles", permissionService.getCurrentUserRoles());
        userInfo.put("permissions", permissionService.getCurrentUserPermissions());
        
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Test if current user has a specific permission
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testPermission(@RequestParam String permission) {
        try {
            Permission perm = Permission.fromAuthority(permission);
            boolean hasPermission = permissionService.hasPermission(perm);
            
            Map<String, Object> result = new HashMap<>();
            result.put("permission", permission);
            result.put("hasPermission", hasPermission);
            result.put("currentUser", permissionService.getCurrentUser() != null ? 
                permissionService.getCurrentUser().getName() : "anonymous");
            
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid permission: " + permission));
        }
    }

    /**
     * Get all available permissions in the system
     */
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailablePermissions() {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, String[]> permissionsByCategory = new HashMap<>();
        permissionsByCategory.put("user", new String[]{
            "user:read", "user:write", "user:update", "user:delete"
        });
        permissionsByCategory.put("product", new String[]{
            "product:read", "product:write", "product:update", "product:delete"
        });
        
        result.put("permissionsByCategory", permissionsByCategory);
        result.put("allPermissions", Permission.values());
        
        return ResponseEntity.ok(result);
    }

    /**
     * Test multiple permissions at once
     */
    @GetMapping("/test-multiple")
    public ResponseEntity<Map<String, Object>> testMultiplePermissions(
            @RequestParam String[] permissions) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Boolean> permissionResults = new HashMap<>();
        
        for (String permissionStr : permissions) {
            try {
                Permission perm = Permission.fromAuthority(permissionStr);
                permissionResults.put(permissionStr, permissionService.hasPermission(perm));
            } catch (IllegalArgumentException e) {
                permissionResults.put(permissionStr, false);
            }
        }
        
        result.put("permissionResults", permissionResults);
        result.put("currentUser", permissionService.getCurrentUser() != null ? 
            permissionService.getCurrentUser().getName() : "anonymous");
        
        return ResponseEntity.ok(result);
    }

    /**
     * Check if user has access to specific resources
     */
    @GetMapping("/resource-access")
    public ResponseEntity<Map<String, Object>> checkResourceAccess() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Boolean> resourceAccess = new HashMap<>();
        
        // Check access to different resources
        resourceAccess.put("canReadUsers", permissionService.hasPermission(Permission.USER_READ));
        resourceAccess.put("canCreateUsers", permissionService.hasPermission(Permission.USER_WRITE));
        resourceAccess.put("canUpdateUsers", permissionService.hasPermission(Permission.USER_UPDATE));
        resourceAccess.put("canDeleteUsers", permissionService.hasPermission(Permission.USER_DELETE));
        
        resourceAccess.put("canReadProducts", permissionService.hasPermission(Permission.PRODUCT_READ));
        resourceAccess.put("canCreateProducts", permissionService.hasPermission(Permission.PRODUCT_WRITE));
        resourceAccess.put("canUpdateProducts", permissionService.hasPermission(Permission.PRODUCT_UPDATE));
        resourceAccess.put("canDeleteProducts", permissionService.hasPermission(Permission.PRODUCT_DELETE));
        
        // Check for combined permissions
        resourceAccess.put("canManageUsers", permissionService.hasAllPermissions(
            Permission.USER_READ, Permission.USER_WRITE, Permission.USER_UPDATE, Permission.USER_DELETE));
        resourceAccess.put("canManageProducts", permissionService.hasAllPermissions(
            Permission.PRODUCT_READ, Permission.PRODUCT_WRITE, Permission.PRODUCT_UPDATE, Permission.PRODUCT_DELETE));
        
        resourceAccess.put("hasAnyUserPermission", permissionService.hasAnyPermission(
            Permission.USER_READ, Permission.USER_WRITE, Permission.USER_UPDATE, Permission.USER_DELETE));
        resourceAccess.put("hasAnyProductPermission", permissionService.hasAnyPermission(
            Permission.PRODUCT_READ, Permission.PRODUCT_WRITE, Permission.PRODUCT_UPDATE, Permission.PRODUCT_DELETE));
        
        result.put("resourceAccess", resourceAccess);
        result.put("currentUser", permissionService.getCurrentUser() != null ? 
            permissionService.getCurrentUser().getName() : "anonymous");
        result.put("userRoles", permissionService.getCurrentUserRoles());
        
        return ResponseEntity.ok(result);
    }
}