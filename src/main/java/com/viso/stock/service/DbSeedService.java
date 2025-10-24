package com.viso.stock.service;

import java.util.Optional;

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
            String pName = p.getAuthority();
            Optional<PermissionEntity> saved = permissionRepository.findByName(p);
            if(saved == null || saved.isEmpty()) {
                PermissionEntity permission = new PermissionEntity(pName);
                permissionRepository.save(permission);
            }
        }
    }
}