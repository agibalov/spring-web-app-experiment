package me.loki2302.controllers;

import me.loki2302.service.ArticleService;
import me.loki2302.service.dto.article.CompleteArticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private ArticleService articleService;
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "new/{categoryId}", method = RequestMethod.GET)
    public String createArticle(@PathVariable int categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("articleModel", new ArticleModel());
        return "article/new";
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "new/{categoryId}", method = RequestMethod.POST)
    public String createArticleDo(
            @CurrentUser Integer currentUserId,
            @PathVariable int categoryId,
            Model model,
            @Validated @ModelAttribute("articleModel") ArticleModel articleModel,
            BindingResult bindingResult) {
                
        if(bindingResult.hasErrors()) {            
            FieldError titleError = bindingResult.getFieldError("title");
            if(titleError != null) {
                model.addAttribute("titleError", titleError.getDefaultMessage());    
            }            
            
            FieldError textError = bindingResult.getFieldError("text");
            if(textError != null) {
                model.addAttribute("textError", textError.getDefaultMessage());
            }
            
            return "article/new";
        }
        
        String title = articleModel.getTitle();
        String text = articleModel.getText();
        logger.info("title: {}", title);
        logger.info("text: {}", text);
        
        int articleId = articleService.createArticle(
                currentUserId,
                categoryId,
                title,
                text);
        
        UriComponents uriComponents = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("/article/{articleId}")
                .build();      
        String articleUrl = uriComponents.expand(articleId).encode().toUriString();
        logger.info("URL: {}", articleUrl);
        
        return String.format("redirect:%s", articleUrl);
    }
    
    @RequestMapping("{articleId}")
    public String viewArticle(@PathVariable int articleId, Model model) {
        CompleteArticle article = articleService.getArticle(articleId);
        model.addAttribute("article", article);
        return "article/index";
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "{articleId}/edit", method = RequestMethod.GET)
    public String editArticle(
            @CurrentUser Integer currentUserId,
            @PathVariable int articleId, 
            Model model) {
                
        CompleteArticle article = articleService.getArticle(articleId);
        // Spring security, any ideas?
        if(article.User.UserId != currentUserId) { // what do i normally do here?
            throw new RuntimeException("the user doesn't own this article");
        }
        
        model.addAttribute("articleId", articleId);
        ArticleModel articleModel = new ArticleModel();
        articleModel.setTitle(article.Title);
        articleModel.setText(article.Text);
        model.addAttribute("articleModel", articleModel);        
        return "article/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "{articleId}/edit", method = RequestMethod.POST)
    public String editArticleDo(@PathVariable int articleId, Model model) {
        // TODO
        throw new RuntimeException();
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "{articleId}/vote/{vote}", method = RequestMethod.POST)
    public String voteDo(
            @CurrentUser Integer currentUserId,
            @PathVariable int articleId,
            @PathVariable int vote) {
        articleService.voteForArticle(currentUserId, articleId, vote);
        return String.format("redirect:/article/%d", articleId);
    }
}
