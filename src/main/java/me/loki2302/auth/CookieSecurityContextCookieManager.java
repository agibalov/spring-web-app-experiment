package me.loki2302.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class CookieSecurityContextCookieManager {
    private final static String AUTH_COOKIE_NAME = "secret_cookie";
    
    public Cookie createAuthenticationCookie(int userId) {
        Cookie authenticationCookie = new Cookie(AUTH_COOKIE_NAME, String.format("%d", userId));
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
        Cookie cookie = getCookie(request);
        if(cookie == null) {
            return null;
        }
        
        return cookie.getValue();
    }
    
    private static Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if(cookieName.equals(AUTH_COOKIE_NAME)) {
                return cookie;
            }
        }
        
        return null;
    }
}
