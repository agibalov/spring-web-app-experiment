package me.loki2302.controllers;

import me.loki2302.service.BlogService;
import me.loki2302.service.CompleteArticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private BlogService blogService;
    
    @RequestMapping("{articleId}")
    public String article(@PathVariable int articleId, Model model) {
        CompleteArticle article = blogService.getCompleteArticle(articleId);
        model.addAttribute("article", article);
        return "article/index";
    }
}