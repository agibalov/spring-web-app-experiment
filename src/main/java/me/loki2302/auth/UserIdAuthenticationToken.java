package me.loki2302.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserIdAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;
    
    private final int userId;
    private final String userName;
    private final String sessionToken;
    
    public UserIdAuthenticationToken(
            int userId, 
            String userName, 
            String sessionToken, 
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);  
        this.userId = userId;
        this.userName = userName;
        this.sessionToken = sessionToken;
    }

    @Override
    public Object getCredentials() {
        return sessionToken;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}