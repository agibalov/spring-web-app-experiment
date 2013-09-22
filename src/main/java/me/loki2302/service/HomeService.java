package me.loki2302.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.CategoryDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.Home;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;
import me.loki2302.service.mappers.BriefCategoryMapper;
import me.loki2302.service.mappers.BriefUserMapper;
import me.loki2302.service.mappers.MappingHelpers;
import me.loki2302.service.mappers.ShortArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    @Autowired
    private CurrentTimeProvider currentTimeProvider;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private ArticleDao articleDao;
          
    @Autowired
    private BriefCategoryMapper briefCategoryMapper;
    
    @Autowired
    private BriefUserMapper briefUserMapper;
    
    @Autowired
    private ShortArticleMapper shortArticleMapper;
                
    public Home getHome(Integer userId, int numberOfRecentArticles) {        
        Home home = new Home();
        
        List<CategoryRow> allCategoryRows = categoryDao.getCategories();
        home.Categories = briefCategoryMapper.makeBriefCategories(allCategoryRows);
        
        List<ArticleRow> articleRows = articleDao.getRecentArticles(numberOfRecentArticles);
            
        Set<Integer> userIds = MappingHelpers.extractUserIds(articleRows);
        List<UserRow> userRows = userDao.getUsers(userIds);
        Map<Integer, BriefUser> briefUsersMap = briefUserMapper.makeBriefUsersMap(userRows);
            
        Set<Integer> categoryIds = MappingHelpers.extractCategoryIds(articleRows);
        List<CategoryRow> categoryRows = categoryDao.getCategories(categoryIds);
        Map<Integer, BriefCategory> briefCategoriesMap = briefCategoryMapper.makeBriefCategoriesMap(categoryRows);
            
        home.MostRecentArticles = shortArticleMapper.makeShortArticles(
                articleRows, 
                briefUsersMap, 
                briefCategoriesMap);
        
        if(userId != null) {
            UserRow user = userDao.getUser(userId);
            home.User = briefUserMapper.makeBriefUser(user);
        }
        
        return home;
    }
}