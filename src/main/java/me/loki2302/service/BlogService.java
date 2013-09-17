package me.loki2302.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.CategoryDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.UserRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private ArticleDao articleDao;
    
    public UserRow createUser(String userName) {
        UserRow existingUser = userDao.findUserByUserName(userName);
        if(existingUser != null) {
            throw new RuntimeException("user already exists");
        }
        
        return userDao.createUser(userName);
    }
    
    public CategoryRow createCategory(String categoryName) {
        return categoryDao.createCategory(categoryName);
    }
    
    public List<CategoryRow> getCategories() {
        return categoryDao.getCategories();
    }
    
    public ArticleRow createArticle(int userId, int categoryId, String title, String text) {
        return articleDao.createArticle(userId, categoryId, title, text, new Date());
    }
        
    public CompleteArticle getCompleteArticle(int articleId) {
        ArticleRow articleRow = articleDao.getArticle(articleId);
        if(articleRow == null) {
            throw new ArticleNotFoundException();
        }
        
        UserRow userRow = userDao.getUser(articleRow.UserId);
        CategoryRow categoryRow = categoryDao.getCategory(articleRow.CategoryId);
        
        CompleteArticle completeArticle = new CompleteArticle();
        completeArticle.Article = articleRow;
        completeArticle.User = userRow;
        completeArticle.Category = categoryRow;
                
        return completeArticle;
    } 
    
    public Category getCategory(int categoryId) {
        CategoryRow categoryRow = categoryDao.getCategory(categoryId);
        if(categoryRow == null) {
            throw new CategoryNotFoundException();
        }
        
        List<ArticleRow> articleRows = articleDao.getArticlesByCategory(categoryId);
        
        Set<Integer> userIds = new HashSet<Integer>();
        for(ArticleRow articleRow : articleRows) {
            userIds.add(articleRow.UserId);
        }
        
        List<UserRow> userRows = userDao.getUsers(userIds);
        
        Map<Integer, UserRow> usersMap = new HashMap<Integer, UserRow>();
        for(UserRow userRow : userRows) {
            usersMap.put(userRow.Id, userRow);
        }
        
        Map<Integer, CategoryRow> categoriesMap = new HashMap<Integer, CategoryRow>();
        categoriesMap.put(categoryRow.Id, categoryRow);
        
        List<CompleteArticle> completeArticles = new ArrayList<CompleteArticle>();
        for(ArticleRow articleRow : articleRows) {
            CompleteArticle completeArticle = new CompleteArticle();
            completeArticle.Article = articleRow;
            completeArticle.Category = categoriesMap.get(articleRow.CategoryId);
            completeArticle.User = usersMap.get(articleRow.UserId);
            completeArticles.add(completeArticle);
        }
        
        Category category = new Category();
        category.Category = categoryRow;
        category.Articles = completeArticles;
        
        return category;
    }
    
    public Home getHome(int numberOfRecentArticles) {
        Home home = new Home();
        home.Categories = categoryDao.getCategories();
        home.MostRecentArticles = articleDao.getRecentArticles(numberOfRecentArticles);        
        return home;
    }
}