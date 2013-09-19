package me.loki2302.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class BlogController {
    @ModelAttribute("authentication")
    public Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}