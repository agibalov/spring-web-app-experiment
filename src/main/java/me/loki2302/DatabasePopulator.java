package me.loki2302;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.service.ArticleService;
import me.loki2302.service.AuthenticationService;
import me.loki2302.service.CategoryService;
import me.loki2302.service.CurrentTimeProvider;
import me.loki2302.service.dto.AuthenticationResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator {
    private final static Logger logger = LoggerFactory.getLogger(DatabasePopulator.class);
    
    @Autowired
    private CurrentTimeProvider currentTimeProvider;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private Generator generator;
    
    @PostConstruct
    public void PopulateDatabase() {
        
        // TODO: user currentTimeProvider to play with time
        
        logger.info("generating users");
        final int numberOfUsers = 23;        
        List<Integer> userIds = new ArrayList<Integer>();
        for(int i = 0; i < numberOfUsers; ++i) {
            String userName = generator.username();
            AuthenticationResult authenticationResult = authenticationService.signUp(userName, "qwerty");
            int userId = authenticationResult.UserId;
            userIds.add(userId);
        }
        
        logger.info("generating categories");
        List<String> categoryNames = Arrays.asList("Porn", "Music", "Programming");
        List<CategoryRow> categoryRows = new ArrayList<CategoryRow>();
        for(String categoryName : categoryNames) {
            CategoryRow categoryRow = categoryService.createCategory(categoryName);
            categoryRows.add(categoryRow);
        }        
        
        logger.info("generating articles");
        final int numberOfArticlesPerUserPerCategory = 7;
        for(int userId : userIds) {
            logger.info("...as user {}", userId);
            for(CategoryRow categoryRow : categoryRows) {
                for(int i = 0; i < numberOfArticlesPerUserPerCategory; ++i) {                    
                    String title = generator.articleTitle();
                    String text = generator.articleMarkdown();                    
                    articleService.createArticle(userId, categoryRow.Id, title, text);
                }
            }
        }
        
        logger.info("finished generating data");
    }
}