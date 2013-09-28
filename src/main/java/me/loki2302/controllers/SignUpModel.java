package me.loki2302.controllers;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class SignUpModel {
    @NotNull
    @NotEmpty
    private String userName;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @NotNull
    private SignUpRole role;
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setRole(SignUpRole role) {
        this.role = role;
    }
    
    public SignUpRole getRole() {
        return role;
    }
    
    public static enum SignUpRole {
        Reader("Reader"),
        Writer("Writer");
        
        private final String roleName; // how does it really work?
        
        SignUpRole(String roleName) {
            this.roleName = roleName;
        }
    }
}