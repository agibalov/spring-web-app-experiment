package me.loki2302.controllers;

import java.util.ArrayList;
import java.util.List;


import me.loki2302.auth.SessionAuthenticationToken;
import me.loki2302.service.AuthenticationService;
import me.loki2302.service.dto.AuthenticationResult;
import me.loki2302.service.exceptions.IncorrectPasswordException;
import me.loki2302.service.exceptions.UserNameAlreadyUsedException;
import me.loki2302.service.exceptions.UserNotRegisteredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private AuthenticationService authenticationService;        
    
    @RequestMapping(value = "/sign-in", method = RequestMethod.GET)
    public String signIn(Model model, UriComponentsBuilder b) {        
        // http://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html
        /*UriComponents uriComponents =
                UriComponentsBuilder.fromUriString("http://example.com/hotels/{hotel}/bookings/{booking}").build();

        URI uri = uriComponents.expand("42", "21").encode().toUri();*/
        
        logger.info("OMG: {}", b.build());        
        
        model.addAttribute("signInModel", new SignInModel());
        return "account/sign-in";
    }
    
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public String signIn(
            Model model,
            @Validated @ModelAttribute("signInModel") SignInModel signInModel,
            BindingResult bindingResult) {
        
        if(bindingResult.hasErrors()) {            
            FieldError userNameError = bindingResult.getFieldError("userName");
            if(userNameError != null) {
                model.addAttribute("userNameError", userNameError.getDefaultMessage());    
            }            
            
            FieldError passwordError = bindingResult.getFieldError("password");
            if(passwordError != null) {
                model.addAttribute("passwordError", passwordError.getDefaultMessage());
            }
        } else {            
            try {
                AuthenticationResult authenticationResult = authenticationService.signIn(
                        signInModel.getUserName(), 
                        signInModel.getPassword());
                                
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
                authorities.add(new SimpleGrantedAuthority("USER"));
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("WHOEVER"));
                
                SessionAuthenticationToken authentication = new SessionAuthenticationToken(
                        authenticationResult.UserId,
                        authenticationResult.UserName,
                        authenticationResult.SessionToken,
                        authorities);
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                return "redirect:/";
            } catch(UserNotRegisteredException e) {
                model.addAttribute("userNameError", "User not registered");
            } catch(IncorrectPasswordException e) {
                model.addAttribute("passwordError", "Incorrect password");
            }
        }        
        
        return "account/sign-in";
    }
    
    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String signUp(Model model) {
        model.addAttribute("signUpModel", new SignUpModel());
        return "account/sign-up";
    }
    
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public String signUp(
            Model model,
            @Validated @ModelAttribute("signUpModel") SignUpModel signUpModel,
            BindingResult bindingResult) {
        
        if(bindingResult.hasErrors()) {            
            FieldError userNameError = bindingResult.getFieldError("userName");
            if(userNameError != null) {
                model.addAttribute("userNameError", userNameError.getDefaultMessage());    
            }            
            
            FieldError passwordError = bindingResult.getFieldError("password");
            if(passwordError != null) {
                model.addAttribute("passwordError", passwordError.getDefaultMessage());
            }
        } else {            
            try {
                AuthenticationResult authenticationResult = authenticationService.signUp(
                        signUpModel.getUserName(), 
                        signUpModel.getPassword());
                
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
                authorities.add(new SimpleGrantedAuthority("USER"));
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("WHOEVER"));
                
                SessionAuthenticationToken authentication = new SessionAuthenticationToken(
                        authenticationResult.UserId,
                        authenticationResult.UserName,
                        authenticationResult.SessionToken,
                        authorities);
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                return "redirect:/";
            } catch(UserNameAlreadyUsedException e) {
                model.addAttribute("userNameError", "User name already used");
            }
        }
        
        return "account/sign-up";
    }
    
    @RequestMapping(value = "/sign-out", method = RequestMethod.GET)
    public String signOut() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
