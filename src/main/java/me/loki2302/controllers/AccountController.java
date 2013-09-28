package me.loki2302.controllers;

import me.loki2302.auth.SessionAuthenticationToken;
import me.loki2302.controllers.SignUpModel.SignUpRole;
import me.loki2302.service.AuthenticationService;
import me.loki2302.service.UserType;
import me.loki2302.service.dto.AuthenticationResult;
import me.loki2302.service.exceptions.IncorrectPasswordException;
import me.loki2302.service.exceptions.UserNameAlreadyUsedException;
import me.loki2302.service.exceptions.UserNotRegisteredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/account")
public class AccountController {    
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private AuthenticationService authenticationService;        
    
    @RequestMapping(value = "/sign-in", method = RequestMethod.GET)
    public String signIn(Model model) {               
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
                
                logger.info("SIGNED IN AS {} ({})", authenticationResult.UserName, authenticationResult.UserType);
                                
                SessionAuthenticationToken authentication = new SessionAuthenticationToken(
                        authenticationResult.UserId,
                        authenticationResult.UserName,
                        authenticationResult.SessionToken,
                        AuthorityUtils.NO_AUTHORITIES);
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
        SignUpModel signUpModel = new SignUpModel();
        signUpModel.setRole(SignUpRole.Reader);
        model.addAttribute("signUpModel", signUpModel);
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
            SignUpRole role = signUpModel.getRole();            
            UserType userType;
            if(role.equals(SignUpRole.Reader)) {
                userType = UserType.Reader;
            } else if(role.equals(SignUpRole.Writer)) {
                userType = UserType.Writer;
            } else {
                throw new RuntimeException("Unknown role");
            }
            
            try {
                AuthenticationResult authenticationResult = authenticationService.signUp(
                        signUpModel.getUserName(), 
                        signUpModel.getPassword(),
                        userType);
                
                logger.info("SIGNED UP AS {} ({})", authenticationResult.UserName, authenticationResult.UserType);
                                
                SessionAuthenticationToken authentication = new SessionAuthenticationToken(
                        authenticationResult.UserId,
                        authenticationResult.UserName,
                        authenticationResult.SessionToken,
                        AuthorityUtils.NO_AUTHORITIES);
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
