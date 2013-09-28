package me.loki2302;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.loki2302.auth.SessionAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

@Component
public class UserRelatedDetailsModelExtender extends HandlerInterceptorAdapter {    
    @Override
    public void postHandle(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Object handler, 
            ModelAndView modelAndView) throws Exception {
        
        if(modelAndView == null) {
            return;
        }
                
        View view = modelAndView.getView();
        if(view != null && view instanceof RedirectView) {
            return;
        }
        
        String viewName = modelAndView.getViewName();
        if(viewName != null && viewName.startsWith("redirect:")) {
            return;
        }
        
        ModelMap modelMap = modelAndView.getModelMap();
        if(modelMap == null) {
            return;
        }
                
        Principal principal = request.getUserPrincipal();
        if(principal == null) {            
            return;
        }
        
        if(!(principal instanceof SessionAuthenticationToken)) {
            throw new IllegalStateException();
        }
        
        SessionAuthenticationToken sessionAuthenticationToken = (SessionAuthenticationToken)principal;
        
        UserDetails userDetails = new UserDetails();
        userDetails.UserId = (Integer)sessionAuthenticationToken.getPrincipal();
        userDetails.UserName = sessionAuthenticationToken.getName();
        userDetails.SessionToken = (String)sessionAuthenticationToken.getCredentials();
        
        String roles = "";
        for(GrantedAuthority authority : sessionAuthenticationToken.getAuthorities()) {
            roles += authority.getAuthority() + " ";
        }
        roles = roles.trim();
        userDetails.Roles = roles;
        
        modelMap.addAttribute("User", userDetails);
    }
}