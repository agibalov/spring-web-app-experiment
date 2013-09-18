package me.loki2302.controllers;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
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
public class AccountController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @ModelAttribute("currentUser")
    public String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return "<NO USER>";
        }
        
        return auth.getName();
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
        
        /*http://stackoverflow.com/questions/9941773/spring-security-authentication-user-manually
            
        Authentication authentication =  new UsernamePasswordAuthenticationToken(person, null, person.getAuthorities());
        log.debug("Logging in with {}", authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);*/
        
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
        }
        
        logger.info("UserName: {}, Password: {}", signInModel.getUserName(), signInModel.getPassword());
        
        return "account/sign-in";
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
