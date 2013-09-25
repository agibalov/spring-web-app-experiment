package me.loki2302.controllers;

import java.security.Principal;

import me.loki2302.auth.SessionAuthenticationToken;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {            
        return parameter.getParameterAnnotation(CurrentUser.class) != null && 
                parameter.getParameterType().equals(Integer.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, 
            ModelAndViewContainer mavContainer, 
            NativeWebRequest webRequest, 
            WebDataBinderFactory binderFactory) throws Exception {
        
        if(!supportsParameter(parameter)) {
            return null;
        }
        
        Principal principal = webRequest.getUserPrincipal();
        if(principal == null) {
            return null;
        }
        
        if(!(principal instanceof SessionAuthenticationToken)) {
            return null;
        }
        
        SessionAuthenticationToken sessionAuthenticationToken = (SessionAuthenticationToken)principal;
        return (Integer)sessionAuthenticationToken.getPrincipal();
    }        
}