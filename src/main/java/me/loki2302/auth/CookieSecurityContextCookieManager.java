package me.loki2302.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class CookieSecurityContextCookieManager {
    private final static String AUTH_COOKIE_NAME = "session-token";
    
    public Cookie createAuthenticationCookie(String cookieValue) {
        Cookie authenticationCookie = new Cookie(AUTH_COOKIE_NAME, cookieValue);
        authenticationCookie.setPath("/");
        return authenticationCookie;
    }
    
    public Cookie createAuthenticationKillerCookie() {
        Cookie authenticationCookie = new Cookie(AUTH_COOKIE_NAME, null);
        authenticationCookie.setPath("/");
        authenticationCookie.setMaxAge(0);
        return authenticationCookie;
    }    
    
    public String getAuthenticationCookieValue(HttpServletRequest request) {
        Cookie cookie = getCookieByName(AUTH_COOKIE_NAME, request);
        if(cookie == null) {
            return null;
        }
        
        return cookie.getValue();
    }
    
    private static Cookie getCookieByName(String cookieOfInterest, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if(cookieName.equals(cookieOfInterest)) {
                return cookie;
            }
        }
        
        return null;
    }
}
