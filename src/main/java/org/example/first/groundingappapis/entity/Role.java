package org.example.first.groundingappapis.entity;

public enum Role {
    USER("ROLE_USER"),
    ;

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

