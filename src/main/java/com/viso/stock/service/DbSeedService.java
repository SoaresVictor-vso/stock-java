package com.viso.stock.service;


import org.springframework.stereotype.Service;

import com.viso.stock.model.Permission;
import com.viso.stock.model.PermissionEntity;
import com.viso.stock.repository.PermissionRepository;

import jakarta.transaction.Transactional;

@Service
public class DbSeedService {
    private final PermissionRepository permissionRepository;

    public DbSeedService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public void seedFromEnums() {
        for (Permission p : Permission.values()) {
            permissionRepository
            .findByName(p.getAuthority())
            .orElseGet(() -> permissionRepository.save(new PermissionEntity(p.name(), "")));
        }
    }
}

// continue with: https://github.com/copilot/c/5de225b0-8000-4b47-ae3b-9ae89ea5c62f