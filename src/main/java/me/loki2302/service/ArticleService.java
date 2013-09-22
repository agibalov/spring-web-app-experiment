package me.loki2302.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.CategoryDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;
import me.loki2302.service.exceptions.ArticleNotFoundException;
import me.loki2302.service.mappers.BriefCategoryMapper;
import me.loki2302.service.mappers.BriefUserMapper;
import me.loki2302.service.mappers.CompleteArticleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    private CurrentTimeProvider currentTimeProvider;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private BriefUserMapper briefUserMapper;
    
    @Autowired
    private BriefCategoryMapper briefCategoryMapper;
    
    @Autowired
    private CompleteArticleMapper completeArticleMapper;
    
    public ArticleRow createArticle(int userId, int categoryId, String title, String text) {
        Date currentTime = currentTimeProvider.getCurrentTime();
        return articleDao.createArticle(
                userId, 
                categoryId, 
                title, 
                text, 
                currentTime);
    }
        
    public CompleteArticle getArticle(int articleId) {
        ArticleRow articleRow = articleDao.getArticle(articleId);
        if(articleRow == null) {
            throw new ArticleNotFoundException();
        }

        List<UserRow> userRows = Arrays.asList(userDao.getUser(articleRow.UserId));
        Map<Integer, BriefUser> briefUsersMap = briefUserMapper.makeBriefUsersMap(userRows);
        
        List<CategoryRow> categoryRows = Arrays.asList(categoryDao.getCategory(articleRow.CategoryId));
        Map<Integer, BriefCategory> briefCategoriesMaps = briefCategoryMapper.makeBriefCategoriesMap(categoryRows);
        
        CompleteArticle completeArticle = completeArticleMapper.makeCompleteArticle(
                articleRow, 
                briefUsersMap, 
                briefCategoriesMaps);
                
        return completeArticle;
    }
}