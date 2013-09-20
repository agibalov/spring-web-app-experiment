package me.loki2302.controllers;

import me.loki2302.service.BlogService;
import me.loki2302.service.dto.article.CompleteArticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/article")
public class ArticleController extends BlogController {
    @Autowired
    private BlogService blogService;
    
    @RequestMapping(value = "new/{categoryId}", method = RequestMethod.GET)
    public String createArticle(@PathVariable int categoryId, Model model) {
        return "article/new";
    }
    
    @RequestMapping(value = "new/{categoryId}", method = RequestMethod.POST)
    public String createArticleDo(@PathVariable int categoryId, Model model) {
        // TODO
        throw new RuntimeException();
    }
    
    @RequestMapping("{articleId}")
    public String viewArticle(@PathVariable int articleId, Model model) {
        CompleteArticle article = blogService.getArticle(articleId);
        model.addAttribute("article", article);
        return "article/index";
    }
    
    @RequestMapping(value = "{articleId}/edit", method = RequestMethod.GET)
    public String editArticle(@PathVariable int articleId, Model model) {
        CompleteArticle article = blogService.getArticle(articleId);
        model.addAttribute("article", article);
        return "article/edit";
    }
    
    @RequestMapping(value = "{articleId}/edit", method = RequestMethod.POST)
    public String editArticleDo(@PathVariable int articleId, Model model) {
        // TODO
        throw new RuntimeException();
    }
}