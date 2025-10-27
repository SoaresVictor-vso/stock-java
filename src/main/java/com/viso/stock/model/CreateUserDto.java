package com.viso.stock.model;

public class CreateUserDto {
    private String email;
    private String password;
    private String fullName;
    private String[] roleName;

    public CreateUserDto() {
    }

    public CreateUserDto(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roleName = new String[]{"Default"};
    }

    public CreateUserDto(String email, String password, String fullName, String[] roleName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String[] getRoleName() {
        return roleName;
    }
}
