package com.udacity.project4.api.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginRequest {
    @NotEmpty(message = "Username cannot be null or empty")
    @NotNull
    private String username;

    @NotEmpty(message = "Password cannot be null or empty")
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
