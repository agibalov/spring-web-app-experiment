package me.loki2302;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator {
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private Generator generator;
    
    @PostConstruct
    public void PopulateDatabase() {
        final int numberOfUsers = 3;        
        List<Integer> userIds = new ArrayList<Integer>();
        for(int i = 0; i < numberOfUsers; ++i) {
            String userName = generator.username();
            int userId = blogService.signInOrSignUp(userName, "qwerty");
            userIds.add(userId);
        }
        
        List<String> categoryNames = Arrays.asList("Porn", "Music", "Programming");
        List<CategoryRow> categoryRows = new ArrayList<CategoryRow>();
        for(String categoryName : categoryNames) {
            CategoryRow categoryRow = blogService.createCategory(categoryName);
            categoryRows.add(categoryRow);
        }        
        
        final int numberOfArticlesPerUserPerCategory = 13;
        for(int userId : userIds) {
            for(CategoryRow categoryRow : categoryRows) {
                for(int i = 0; i < numberOfArticlesPerUserPerCategory; ++i) {                    
                    String title = generator.articleTitle();
                    String text = generator.articleMarkdown();                    
                    blogService.createArticle(userId, categoryRow.Id, title, text);
                }
            }
        }
    }
}