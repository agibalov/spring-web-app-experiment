package me.loki2302.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class DummyAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(DummyAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("authentication is {}", authentication);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        logger.info("supports {}?", authentication);
        return true;
    }
}