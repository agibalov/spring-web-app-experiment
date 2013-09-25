package me.loki2302.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

public class CookieSecurityContextRepository implements SecurityContextRepository {
    private final static Logger logger = LoggerFactory.getLogger(CookieSecurityContextRepository.class);
    
    private final CookieSecurityContextCookieManager cookieManager;
    
    public CookieSecurityContextRepository(CookieSecurityContextCookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }
    
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        logger.info("{} loadContext called", requestResponseHolder.getRequest().getRequestURI());
        
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();       
        
        CookieSecurityContextRepositoryResponseWrapper responseWrapper = 
                new CookieSecurityContextRepositoryResponseWrapper(
                        cookieManager, 
                        response, 
                        false); 
        requestResponseHolder.setResponse(responseWrapper);
        
        String authCookieValue = cookieManager.getAuthenticationCookieValue(request);
        if(authCookieValue == null) {
            logger.info("didn't find auth cookie -> returning empty context");
            return SecurityContextHolder.createEmptyContext();
        }       
    
        logger.info("found auth cookie, trying to read");
        
        int userId;
        try {
            userId = Integer.parseInt(authCookieValue);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Authentication cookie contains garbage");
        }
        
        logger.info("read cookie as {}", userId);
        
        logger.info("user id is {}", userId);
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
        authorities.add(new SimpleGrantedAuthority("USER"));
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        authorities.add(new SimpleGrantedAuthority("WHOEVER"));
        UserIdAuthenticationToken authentication = new UserIdAuthenticationToken(userId, authorities);
        authentication.setAuthenticated(true);
        
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        
        logger.info("returning populated security context");
        
        return securityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        logger.info("{} saveContext called {}", request.getRequestURI(), context);        
        CookieSecurityContextRepositoryResponseWrapper responseWrapper = (CookieSecurityContextRepositoryResponseWrapper)response;
        if(!responseWrapper.isContextSaved()) {
            logger.info("context is not saved, saving it");
            responseWrapper.saveContext(context);
        } else {
            logger.info("context already saved");
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        logger.info("{} containsContext called", request.getRequestURI());
        
        String authCookieValue = cookieManager.getAuthenticationCookieValue(request);
        boolean authCookieIsNotNull = authCookieValue != null;
        
        logger.info("auth cookie is {}", authCookieIsNotNull);
        return authCookieIsNotNull;
    }   
}