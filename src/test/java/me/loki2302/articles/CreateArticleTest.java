package me.loki2302.articles;

import me.loki2302.service.AuthenticationService;
import me.loki2302.service.CategoryService;
import me.loki2302.service.UserType;
import me.loki2302.service.dto.AuthenticationResult;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateArticleTest extends AbstractArticleServiceTest {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Test
    public void canCreateArticle() {
        int categoryId = categoryService.createCategory("my category");
        AuthenticationResult authenticationResult = 
                authenticationService.signUp("loki2302", "qwerty", UserType.Writer);
        int userId = authenticationResult.UserId;
        articleService.createArticle(userId, categoryId, "title", "text");
    }
}