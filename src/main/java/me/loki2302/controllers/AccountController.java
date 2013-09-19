package me.loki2302.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import me.loki2302.service.BlogService;
import me.loki2302.service.IncorrectPasswordException;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/account")
public class AccountController extends BlogController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private BlogService blogService;
    
    @ModelAttribute("currentUser")
    public String currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return "authentication is null";
        }
        
        return String.format(
                "name=%s, credentials=%s, details=%s, authorities=%s, isAuthenticated=%b", 
                authentication.getName(),
                authentication.getCredentials(),
                authentication.getDetails(),
                authentication.getAuthorities(),
                authentication.isAuthenticated());
    }
    
    @RequestMapping(value = "/sign-in", method = RequestMethod.GET)
    public String signIn(Model model) {        
        model.addAttribute("signInModel", new SignInModel());
        return "account/sign-in";
    }
    
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public String signInDo(
            Model model,
            @Validated @ModelAttribute("signInModel") SignInModel signInModel,
            BindingResult bindingResult) {
                
        logger.info("Sign In request");
        
        if(bindingResult.hasErrors()) {
            logger.info("There are errors");
            for(ObjectError objectError : bindingResult.getAllErrors()) {
                logger.info("Error: {}", objectError);
            }
            
            FieldError userNameError = bindingResult.getFieldError("userName");
            if(userNameError != null) {
                model.addAttribute("userNameError", userNameError.getDefaultMessage());    
            }            
            
            FieldError passwordError = bindingResult.getFieldError("password");
            if(passwordError != null) {
                model.addAttribute("passwordError", passwordError.getDefaultMessage());
            }
        } else {
            logger.info("There are no errors");
            
            try {
                int userId = blogService.signInOrSignUp(
                        signInModel.getUserName(), 
                        signInModel.getPassword());
                
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
                authorities.add(new SimpleGrantedAuthority("USER"));
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("WHOEVER"));
                
                Authentication authentication = new UserIdAuthenticationToken(
                        userId,
                        authorities);
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                return "redirect:/";
            } catch(IncorrectPasswordException e) {
                model.addAttribute("passwordError", "Incorrect password");
            }
        }
        
        logger.info("UserName: {}, Password: {}", signInModel.getUserName(), signInModel.getPassword());
        
        return "account/sign-in";
    }
    
    @RequestMapping(value = "/sign-out", method = RequestMethod.GET)
    public String signOut() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
    
    public static class UserIdAuthenticationToken extends AbstractAuthenticationToken {
        private static final long serialVersionUID = 1L;
        
        private final int userId; 
        
        public UserIdAuthenticationToken(int userId, Collection<? extends GrantedAuthority> authorities) {
            super(authorities);  
            this.userId = userId;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return userId;
        }
    }
    
    public static class SignInModel {
        @NotNull
        @NotEmpty
        private String userName;
        
        @NotNull
        @NotEmpty
        private String password;
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getPassword() {
            return password;
        }
    }
}
