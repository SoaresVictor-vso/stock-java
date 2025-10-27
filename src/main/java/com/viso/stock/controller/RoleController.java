package com.viso.stock.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viso.stock.model.CreateRoleDto;
import com.viso.stock.model.RoleEntity;
import com.viso.stock.service.RoleService;

@Controller
@RequestMapping("/public/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody CreateRoleDto data) {
        UUID roleId = roleService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleId);
    }   

    @GetMapping
    public ResponseEntity<List<RoleEntity>> findAll() {
        List<RoleEntity> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

}
