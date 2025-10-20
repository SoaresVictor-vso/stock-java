package com.viso.stock.model;

public enum Permission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    PRODUCT_UPDATE("product:update"),
    PRODUCT_DELETE("product:delete");

    private final String authority;

    Permission(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
