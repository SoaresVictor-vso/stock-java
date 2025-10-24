package com.viso.stock.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name" })
})
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    @Convert(converter = PermissionConverter.class)
    private Permission name;

    protected PermissionEntity() {}

    public PermissionEntity(String name) {
        this.name = Permission.fromAuthority(name);
    }

    public PermissionEntity(Permission namePermission) {
        this.name = namePermission;
    }

    public Permission getName() {
        return name;
    }
}
