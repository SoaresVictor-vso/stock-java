package com.viso.stock.model;

import java.util.Set;

public class Role {
    private final Set<Permission> permissions;

    public Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
