package com.viso.stock.model;

import java.util.Set;
import java.util.UUID;

public class TokenUserEntity {
    private UUID id;
    private String name;
    private Set<RoleEntity> roles;

    public TokenUserEntity(UUID id, String name, Set<RoleEntity> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }
}
