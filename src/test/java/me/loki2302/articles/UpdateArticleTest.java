package me.loki2302.articles;

import static org.junit.Assert.assertEquals;
import me.loki2302.service.AuthenticationService;
import me.loki2302.service.CategoryService;
import me.loki2302.service.UserType;
import me.loki2302.service.dto.AuthenticationResult;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.exceptions.AccessToArticleDeniedException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateArticleTest extends AbstractArticleServiceTest {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Test
    public void canUpdateArticle() {
        int categoryId = categoryService.createCategory("my category");
        AuthenticationResult authenticationResult = 
                authenticationService.signUp("loki2302", "qwerty", UserType.Writer);
        int userId = authenticationResult.UserId;
        int articleId = articleService.createArticle(
                userId, 
                categoryId, 
                "test article", 
                "test article text");
        
        articleService.updateArticle(userId, articleId, "new title", "new text");
        CompleteArticle article = articleService.getArticle(null, articleId);
        assertEquals("new title", article.Title);
        assertEquals("new text", article.Text);
    }
    
    @Test(expected = AccessToArticleDeniedException.class)
    public void cantUpdateArticleUserDoesntOwn() {
        int categoryId = categoryService.createCategory("my category");
        AuthenticationResult authenticationResult = 
                authenticationService.signUp("loki2302", "qwerty", UserType.Writer);
        int userId = authenticationResult.UserId;
        int articleId = articleService.createArticle(
                userId, 
                categoryId, 
                "test article", 
                "test article text");
        
        AuthenticationResult hackerAuthenticationResult = 
                authenticationService.signUp("hacker", "qwerty", UserType.Writer);
        int hackerUserId = hackerAuthenticationResult.UserId;
        articleService.updateArticle(hackerUserId, articleId, "new title", "new text");
    }
}