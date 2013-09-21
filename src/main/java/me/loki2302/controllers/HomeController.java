package me.loki2302.controllers;

import java.security.Principal;

import me.loki2302.service.BlogService;
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
    private BlogService blogService;
        
    @RequestMapping
    public String index(Model model/*, Principal principal, UserIdAuthenticationToken t*/) {
        /*logger.info("PRINCIPAL: {}", principal);
        logger.info("TOKEN: {}", t);*/
        
        Integer userId = null;
        /*if(t != null) {
            userId = (Integer)t.getPrincipal();
        }*/
        
        Home home = blogService.getHome(userId, 3);
        model.addAttribute("home", home);
        return "home/index";
    }
}
