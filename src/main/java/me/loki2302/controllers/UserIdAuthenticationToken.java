package me.loki2302.controllers;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserIdAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;
    
    private final int userId; 
    
    public UserIdAuthenticationToken(int userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);  
        this.userId = userId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}