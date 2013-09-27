package me.loki2302.controllers;

import java.security.Principal;

import me.loki2302.auth.SessionAuthenticationToken;
import me.loki2302.service.ArticleService;
import me.loki2302.service.CategoryService;
import me.loki2302.service.UserService;
import me.loki2302.service.dto.Home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
@RequestMapping("/")
public class HomeController {   
    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private UserService userService;
           
    @RequestMapping
    public String index(
            Model model, 
            Principal principal, 
            SessionAuthenticationToken sessionAuthenticationToken, 
            NativeWebRequest nativeWebRequest,
            @CurrentUser Integer currentUserId) {
        
        logger.info("HOME");
        logger.info("PRINCIPAL: {}", principal);
        logger.info("TOKEN: {}", sessionAuthenticationToken);
        logger.info("SECURITY CONTEXT: {}", SecurityContextHolder.getContext().getAuthentication());
        logger.info("WEB REQUEST: {}", nativeWebRequest.getUserPrincipal());
        logger.info("CURRENT USER: {}", currentUserId);       
        
        Home home = new Home();        
        home.Categories = categoryService.getBriefCategories();            
        home.MostRecentArticles = articleService.getMostRecentArticles(3);        
        model.addAttribute("home", home);
        return "home/index";
    }
}
