package me.loki2302.controllers;

public enum SignUpRole {
    Reader("Reader"),
    Writer("Writer");
    
    private final String roleName; // how does it really work?
    
    SignUpRole(String roleName) {
        this.roleName = roleName;
    }
}