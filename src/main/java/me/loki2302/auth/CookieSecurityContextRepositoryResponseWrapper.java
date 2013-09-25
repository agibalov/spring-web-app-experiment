package me.loki2302.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;

public class CookieSecurityContextRepositoryResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {
    private final static Logger logger = LoggerFactory.getLogger(CookieSecurityContextRepositoryResponseWrapper.class);
    
    private final CookieSecurityContextCookieManager cookieManager;
    
    public CookieSecurityContextRepositoryResponseWrapper(
            CookieSecurityContextCookieManager cookieManager, 
            HttpServletResponse response, 
            boolean disableUrlRewriting) {
        super(response, disableUrlRewriting);
        this.cookieManager = cookieManager;
    }

    @Override
    protected void saveContext(SecurityContext context) {        
        Authentication authentication = context.getAuthentication();
        if(authentication == null) {
            logger.info("auth is null, killing the cookie");
            Cookie authCookie = cookieManager.createAuthenticationKillerCookie();
            addCookie(authCookie);
            return;
        }
        
        if(!(authentication instanceof UserIdAuthenticationToken)) {
            logger.info("auth type is something weird: {} -> throwing", authentication);
            throw new RuntimeException("Unsupported authentication type");
        }
        
        UserIdAuthenticationToken userIdAuthenticationToken = (UserIdAuthenticationToken)authentication;
        int userId = (Integer)userIdAuthenticationToken.getPrincipal();
        
        logger.info("auth type is what i know, userId is {}, saving cookie", userId);
        
        Cookie authCookie = cookieManager.createAuthenticationCookie(userId);
        addCookie(authCookie);
    }
}