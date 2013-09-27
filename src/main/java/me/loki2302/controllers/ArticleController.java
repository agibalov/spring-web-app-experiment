package me.loki2302.controllers;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.service.ArticleService;
import me.loki2302.service.dto.article.CompleteArticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private ArticleService articleService;
    
    @RequestMapping(value = "new/{categoryId}", method = RequestMethod.GET)
    public String createArticle(@PathVariable int categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("articleModel", new ArticleModel());
        return "article/new";
    }
    
    @RequestMapping(value = "new/{categoryId}", method = RequestMethod.POST)
    public String createArticleDo(
            @CurrentUser Integer currentUserId,
            @PathVariable int categoryId,
            Model model,
            @Validated @ModelAttribute("articleModel") ArticleModel articleModel,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder) {
        
        if(currentUserId == null) {
            throw new RuntimeException("user should be authenticated to post articles");
        }
        
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
        
        ArticleRow articleRow = articleService.createArticle(
                currentUserId,
                categoryId,
                title,
                text);
        
        UriComponents uriComponents = uriComponentsBuilder.replacePath("/article/{articleId}").build();
        String articleUrl = uriComponents.expand(articleRow.Id).encode().toUriString();
        logger.info("URL: {}", articleUrl);
        
        return String.format("redirect:%s", articleUrl);
    }
    
    @RequestMapping("{articleId}")
    public String viewArticle(@PathVariable int articleId, Model model) {
        CompleteArticle article = articleService.getArticle(articleId);
        model.addAttribute("article", article);
        return "article/index";
    }
    
    @RequestMapping(value = "{articleId}/edit", method = RequestMethod.GET)
    public String editArticle(@PathVariable int articleId, Model model) {
        CompleteArticle article = articleService.getArticle(articleId);
        model.addAttribute("article", article);
        return "article/edit";
    }
    
    @RequestMapping(value = "{articleId}/edit", method = RequestMethod.POST)
    public String editArticleDo(@PathVariable int articleId, Model model) {
        // TODO
        throw new RuntimeException();
    }
}