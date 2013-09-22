package me.loki2302.controllers;

import java.util.ArrayList;
import java.util.List;


import me.loki2302.auth.UserIdAuthenticationToken;
import me.loki2302.service.BlogService;
import me.loki2302.service.IncorrectPasswordException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.UriComponentsBuilder;

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
                
                UserIdAuthenticationToken authentication = new UserIdAuthenticationToken(
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
        logger.info("SIGN OUT");
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
