package com.viso.stock.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PermissionConverter implements AttributeConverter<Permission, String> {

    @Override
    public String convertToDatabaseColumn(Permission permission) {
        if (permission == null) {
            return null;
        }
        return permission.getAuthority();
    }

    @Override
    public Permission convertToEntityAttribute(String authority) {
        if (authority == null) {
            return null;
        }
        
        for (Permission p : Permission.values()) {
            if (p.getAuthority().equals(authority)) {
                return p;
            }
        }
        
        throw new IllegalArgumentException("Unknown permission authority: " + authority);
    }
}