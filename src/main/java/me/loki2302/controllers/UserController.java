package me.loki2302.controllers;

import me.loki2302.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController extends BlogController {
    @Autowired
    private BlogService blogService;
    
    @RequestMapping("{userId}")
    public String user(@PathVariable int userId, Model model) {
        model.addAttribute("user", blogService.getUser(userId));
        return "user/index";
    }
}