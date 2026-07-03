package com.LibGO.Library.dto;

public class LoginRequest {
    private String collageEmailId;
    private String password;

    public void setCollageEmailId(String collageEmailId) {
        this.collageEmailId = collageEmailId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollageEmailId() {
        return collageEmailId;
    }

    public String getPassword() {
        return password;
    }
}
