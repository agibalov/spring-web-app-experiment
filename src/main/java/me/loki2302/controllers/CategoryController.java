package me.loki2302.controllers;

import me.loki2302.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private BlogService blogService;
    
    @RequestMapping
    public String listOfCategories(Model model) {
        model.addAttribute("categories", blogService.getCategories(3));            
        return "category/index";
    }
    
    @RequestMapping("{categoryId}")
    public String category(@PathVariable int categoryId, Model model) {
        model.addAttribute("category", blogService.getCategory(categoryId));
        return "category/category";
    }
}
