package me.loki2302.service;

import java.util.Date;
import java.util.List;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.ArticleDao.ArticleRow;
import me.loki2302.dao.CategoryDao;
import me.loki2302.dao.CategoryDao.CategoryRow;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.UserDao.UserRow;

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
    
    public ArticleRow getArticle(int articleId) {
        return articleDao.getArticle(articleId);
    }
    
    public Category getCategory(int categoryId) {
        CategoryRow categoryRow = categoryDao.getCategory(categoryId);
        if(categoryRow == null) {
            throw new RuntimeException("no such category");
        }
        
        List<ArticleRow> articleRows = articleDao.getArticlesByCategory(categoryId);
        
        Category category = new Category();
        category.Category = categoryRow;
        category.Articles = articleRows;
        
        return category;
    }
    
    public Home getHome(int numberOfRecentArticles) {
        Home home = new Home();
        home.MostRecentArticles = articleDao.getRecentArticles(numberOfRecentArticles);
        return home;
    }
    
    public static class Category {
        public CategoryRow Category;
        public List<ArticleRow> Articles;
    }
    
    public static class Home {
        public List<ArticleRow> MostRecentArticles;
    }
}