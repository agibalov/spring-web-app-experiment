package me.loki2302.controllers;

import me.loki2302.dao.rows.Page;
import me.loki2302.service.ArticleService;
import me.loki2302.service.CommentService;
import me.loki2302.service.UserService;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;

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
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CommentService commentService;
    
    @RequestMapping("{userId}")
    public String user(@PathVariable int userId, Model model) {
        model.addAttribute("user", userService.getCompleteUser(userId));
        return "user/index";
    }
    
    @RequestMapping("{userId}/articles/")
    public String userArticles(
            @PathVariable int userId, 
            int itemsPerPage, 
            int page, 
            Model model) {
        
        Page<BriefArticle> briefArticlesPage = articleService.getArticlesByUser(userId, itemsPerPage, page);
        model.addAttribute("userId", userId);
        model.addAttribute("articles", briefArticlesPage);
        return "user/articles";
    }
    
    @RequestMapping("{userId}/comments/")
    public String userComments(
            @PathVariable int userId, 
            int itemsPerPage, 
            int page, 
            Model model) {
        
        Page<Comment> commentsPage = commentService.getCommentsByUser(userId, itemsPerPage, page);
        model.addAttribute("userId", userId);
        model.addAttribute("comments", commentsPage);
        return "user/comments";
    }
}