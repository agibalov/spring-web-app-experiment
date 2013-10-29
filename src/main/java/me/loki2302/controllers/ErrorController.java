package me.loki2302.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("statusCode", request.getAttribute("javax.servlet.error.status_code"));
        model.addAttribute("message", request.getAttribute("javax.servlet.error.message"));
        return "error/error";
    }
}