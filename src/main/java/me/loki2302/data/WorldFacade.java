package me.loki2302.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.service.ArticleService;
import me.loki2302.service.AuthenticationService;
import me.loki2302.service.CategoryService;
import me.loki2302.service.UserType;
import me.loki2302.service.dto.AuthenticationResult;
import me.loki2302.service.exceptions.UserNameAlreadyUsedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorldFacade {
    private final Random random = new Random();
    public final List<Integer> userIds = new ArrayList<Integer>();
    public final List<Integer> categoryIds = new ArrayList<Integer>();
    public final List<Integer> articleIds = new ArrayList<Integer>();
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private Generator generator;
    
    public void makeRandomUser() {
        while(true) {
            try {
                String userName = generator.username();
                AuthenticationResult authenticationResult = authenticationService.signUp(
                        userName, 
                        "qwerty", 
                        UserType.Writer);
                int userId = authenticationResult.UserId;
                userIds.add(userId);
                break;
            } catch(UserNameAlreadyUsedException e) {                
            }
        }
    }       
    
    public void makeRandomCategory() {
        String categoryName = String.format("Category#%d", categoryIds.size() + 1);
        CategoryRow categoryRow = categoryService.createCategory(categoryName);
        categoryIds.add(categoryRow.Id);
    }
    
    public void makeRandomArticle() {
        int userId = getExistingUserOrCreateANewOne();
        int categoryId = getExistingCategoryOrCreateANewOne();
        String title = generator.articleTitle();
        String text = generator.articleMarkdown();
        int articleId = articleService.createArticle(userId, categoryId, title, text);
        articleIds.add(articleId);
    }
    
    private int getExistingUserOrCreateANewOne() {
        if(userIds.isEmpty()) {
            makeRandomUser();
        }
        
        return userIds.get(random.nextInt(userIds.size()));
    }
    
    private int getExistingCategoryOrCreateANewOne() {
        if(categoryIds.isEmpty()) {
            makeRandomCategory();
        }
        
        return categoryIds.get(random.nextInt(categoryIds.size()));
    }
}