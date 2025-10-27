package com.viso.stock.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viso.stock.model.CreateRoleDto;
import com.viso.stock.model.Permission;
import com.viso.stock.model.PermissionEntity;
import com.viso.stock.model.RoleEntity;
import com.viso.stock.repository.PermissionRepository;
import com.viso.stock.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;

    public RoleService(RoleRepository roleRepo, PermissionRepository permissionRepo) {
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    public UUID create(CreateRoleDto data) {
        // Resolve each incoming permission: reuse existing DB row when present,
        // otherwise create and save a new PermissionEntity.
        Set<PermissionEntity> perms = data.permissions.stream()
            .map(authority -> {
                Permission permEnum = Permission.fromAuthority(authority);
                return permissionRepo.findByName(permEnum)
                    .orElseGet(() -> permissionRepo.save(new PermissionEntity(permEnum)));
            })
            .collect(Collectors.toSet());

        RoleEntity role = new RoleEntity(data.name, perms);
        return roleRepo.save(role).getId();
    }

    public List<RoleEntity> findAll() {
        return roleRepo.findAll();
    }

    @Transactional
    public RoleEntity createRoleWithPermissions(String name, Set<UUID> permissionIds) {
        // Option A: load actual entities
        // Set<PermissionEntity> perms = permissionRepo.findAllById(permissionIds).stream().collect(Collectors.toSet());

        // Option B (lighter): attach references (no SELECTs for full entity)
        Set<PermissionEntity> perms = permissionIds.stream()
            .map(permissionRepo::getReferenceById)
            .collect(Collectors.toSet());

        RoleEntity role = new RoleEntity(name, perms);

        role = roleRepo.save(role); // will insert role and role_permissions rows only
        return role;
    }
    
    public Set<RoleEntity> findByNames(String[] names) {
        return new HashSet<>(roleRepo.findByNameIn(names));
    }
}
