package me.loki2302.controllers;

import me.loki2302.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping("{userId}")
    public String user(@PathVariable int userId, Model model) {
        model.addAttribute("user", userService.getCompleteUser(userId));
        return "user/index";
    }
}