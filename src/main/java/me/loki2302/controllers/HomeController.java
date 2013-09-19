package me.loki2302.controllers;

import me.loki2302.service.BlogService;
import me.loki2302.service.dto.Home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController extends BlogController {   
    @Autowired
    private BlogService blogService;
        
    @RequestMapping
    public String index(Model model) {
        Home home = blogService.getHome(3);
        model.addAttribute("home", home);
        return "home/index";
    }
}
