package me.loki2302.jadehelpers;

import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityHelper {
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }
}