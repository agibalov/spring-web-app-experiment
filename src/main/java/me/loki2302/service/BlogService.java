package me.loki2302.service;

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
        return articleDao.createArticle(userId, categoryId, title, text);
    }
}