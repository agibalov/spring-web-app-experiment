package me.loki2302.controllers;

import java.security.Principal;

import me.loki2302.auth.UserIdAuthenticationToken;
import me.loki2302.service.ArticleService;
import me.loki2302.service.CategoryService;
import me.loki2302.service.UserService;
import me.loki2302.service.dto.Home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController extends BlogController {   
    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private UserService userService;
           
    @RequestMapping
    public String index(Model model, Principal principal, UserIdAuthenticationToken t) {
        logger.info("HOME");
        logger.info("PRINCIPAL: {}", principal);
        logger.info("TOKEN: {}", t);
        
        Home home = new Home();        
        home.Categories = categoryService.getBriefCategories();            
        home.MostRecentArticles = articleService.getMostRecentArticles(3);
        
        if(t != null) {            
            home.User = userService.getBriefUser((Integer)t.getPrincipal());
        }
        
        model.addAttribute("home", home);
        return "home/index";
    }
}
