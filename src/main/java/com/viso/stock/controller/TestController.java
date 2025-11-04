package com.viso.stock.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viso.stock.service.PermissionService;

@RestController
@RequestMapping("/test")
public class TestController {

    private final PermissionService permissionService;

    public TestController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Simple test endpoint to verify authentication and permissions work
     */
    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> testAuth() {
        Map<String, Object> response = new HashMap<>();
        
        if (permissionService.getCurrentUser() != null) {
            response.put("status", "authenticated");
            response.put("user", permissionService.getCurrentUser().getName());
            response.put("userId", permissionService.getCurrentUser().getId());
            response.put("roles", permissionService.getCurrentUserRoles());
            response.put("permissions", permissionService.getCurrentUserPermissions());
        } else {
            response.put("status", "not authenticated");
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Test endpoint that requires no authentication (for testing purposes)
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> testPublic() {
        return ResponseEntity.ok(Map.of(
            "message", "This is a public endpoint",
            "timestamp", java.time.Instant.now().toString()
        ));
    }
}