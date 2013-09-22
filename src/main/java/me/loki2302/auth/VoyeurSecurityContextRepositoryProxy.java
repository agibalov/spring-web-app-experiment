package me.loki2302.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

public class VoyeurSecurityContextRepositoryProxy implements SecurityContextRepository {
    private final static Logger logger = LoggerFactory.getLogger(VoyeurSecurityContextRepositoryProxy.class);

    private final SecurityContextRepository httpSessionSecurityContextRepository;
    
    public VoyeurSecurityContextRepositoryProxy(SecurityContextRepository httpSessionSecurityContextRepository) {
        this.httpSessionSecurityContextRepository = httpSessionSecurityContextRepository;
    }
    
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        // read session token from cookies and return it (SecurityContex == Authentication)  
        // requestResponseHolder.getRequest().setAttribute("stuff", "qwerty"); // store things in here?
        logger.info("loadContext() {} {}", requestResponseHolder.getRequest(), requestResponseHolder.getResponse());
        return httpSessionSecurityContextRepository.loadContext(requestResponseHolder);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // save SecurityContext (Authentication) to cookies
        logger.info("saveContext() {}, {}, {}", new Object[] {context, request, response});
        httpSessionSecurityContextRepository.saveContext(context, request, response);            
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        // is there SecurityContext (Authentication) in cookies?
        logger.info("containsContext() {}", request);
        return httpSessionSecurityContextRepository.containsContext(request);
    }        
}