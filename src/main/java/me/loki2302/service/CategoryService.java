package me.loki2302.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.ArticleVoteDao;
import me.loki2302.dao.CategoryDao;
import me.loki2302.dao.CommentDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleCommentCountRow;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.ArticleVoteStatsRow;
import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.Page;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.ShortArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.category.CompleteCategory;
import me.loki2302.service.dto.category.ShortCategory;
import me.loki2302.service.dto.user.BriefUser;
import me.loki2302.service.exceptions.CategoryNotFoundException;
import me.loki2302.service.mappers.ArticleVoteStatsMapper;
import me.loki2302.service.mappers.BriefArticleMapper;
import me.loki2302.service.mappers.BriefCategoryMapper;
import me.loki2302.service.mappers.BriefUserMapper;
import me.loki2302.service.mappers.CommentMapper;
import me.loki2302.service.mappers.CompleteCategoryMapper;
import me.loki2302.service.mappers.MappingHelpers;
import me.loki2302.service.mappers.ShortArticleMapper;
import me.loki2302.service.mappers.ShortCategoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private CommentDao commentDao;
    
    @Autowired
    private ArticleVoteDao articleVoteDao;
    
    @Autowired
    private BriefUserMapper briefUserMapper;       
    
    @Autowired
    private BriefCategoryMapper briefCategoryMapper;
    
    @Autowired
    private BriefArticleMapper briefArticleMapper;
    
    @Autowired
    private ShortCategoryMapper shortCategoryMapper;
    
    @Autowired
    private ShortArticleMapper shortArticleMapper;
    
    @Autowired
    private CompleteCategoryMapper completeCategoryMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private ArticleVoteStatsMapper articleVoteStatsMapper;
    
    public int createCategory(String categoryName) {
        return categoryDao.createCategory(categoryName);
    }
    
    public List<BriefCategory> getBriefCategories() {
        List<CategoryRow> allCategoryRows = categoryDao.getCategories();
        return briefCategoryMapper.makeBriefCategories(allCategoryRows);
    }
    
    public List<ShortCategory> getCategories(int numberOfRecentArticles) {
        List<CategoryRow> categoryRows = categoryDao.getCategories();
        
        Set<Integer> categoryIds = MappingHelpers.extractCategoryIdsFromCategoryRows(categoryRows);
        List<ArticleRow> articleRows = articleDao.getRecentArticlesForCategories(
                categoryIds, 
                numberOfRecentArticles);
        
        Set<Integer> userIds = MappingHelpers.extractUserIdsFromArticleRows(articleRows);
        List<UserRow> userRows = userDao.getUsers(userIds);
        Map<Integer, BriefUser> briefUsersMap = briefUserMapper.makeBriefUsersMap(userRows);
        
        Set<Integer> articleIds = MappingHelpers.extractArticleIdsFromArticleRows(articleRows);
        
        List<ArticleCommentCountRow> articleCommentCountRows = commentDao.getCommentCountsByArticleIds(articleIds);
        Map<Integer, Integer> articleCommentCountsMap = commentMapper.makeArticleCommentCountMap(articleCommentCountRows);
        
        List<ArticleVoteStatsRow> articleVoteStatsRows = articleVoteDao.getVoteStatsByArticleIds(articleIds);
        Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap = articleVoteStatsMapper.makeArticleVoteStatsMap(articleVoteStatsRows);
        
        Map<Integer, BriefCategory> briefCategoriesMap = briefCategoryMapper.makeBriefCategoriesMap(categoryRows);
        List<BriefArticle> briefArticles = briefArticleMapper.makeBriefArticles(
                articleRows, 
                articleCommentCountsMap,
                articleVoteStatsMap,
                briefUsersMap, 
                briefCategoriesMap);
        
        List<ShortCategory> shortCategories = shortCategoryMapper.makeShortCategories(
                categoryRows, 
                briefArticles);
        
        return shortCategories;
    }
    
    public CompleteCategory getCategory(int categoryId, int articlesPerPage, int page) {
        CategoryRow categoryRow = categoryDao.getCategory(categoryId);
        if(categoryRow == null) {
            throw new CategoryNotFoundException();
        }
        
        Page<ArticleRow> articleRowsPage = articleDao.getArticlesByCategory(
                categoryId, 
                articlesPerPage, 
                page);
        
        Set<Integer> userIds = MappingHelpers.extractUserIdsFromArticleRows(articleRowsPage.Items);
        List<UserRow> userRows = userDao.getUsers(userIds);
        Map<Integer, BriefUser> briefUsersMap = briefUserMapper.makeBriefUsersMap(userRows);
        
        Set<Integer> articleIds = MappingHelpers.extractArticleIdsFromArticleRows(articleRowsPage.Items);
        
        List<ArticleCommentCountRow> articleCommentCountRows = commentDao.getCommentCountsByArticleIds(articleIds);
        Map<Integer, Integer> articleCommentCountsMap = commentMapper.makeArticleCommentCountMap(articleCommentCountRows);
        
        List<ArticleVoteStatsRow> articleVoteStatsRows = articleVoteDao.getVoteStatsByArticleIds(articleIds);
        Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap = articleVoteStatsMapper.makeArticleVoteStatsMap(articleVoteStatsRows);
        
        List<CategoryRow> categoryRows = Arrays.asList(categoryRow);
        Map<Integer, BriefCategory> briefCategoriesMaps = briefCategoryMapper.makeBriefCategoriesMap(categoryRows);
        
        List<ShortArticle> shortArticles = shortArticleMapper.makeShortArticles(
                articleRowsPage.Items,
                articleCommentCountsMap,
                articleVoteStatsMap,
                briefUsersMap,
                briefCategoriesMaps);
        
        Page<ShortArticle> pageData = new Page<ShortArticle>();
        pageData.NumberOfItems = articleRowsPage.NumberOfItems;
        pageData.ItemsPerPage = articleRowsPage.ItemsPerPage;
        pageData.NumberOfPages = articleRowsPage.NumberOfPages;
        pageData.CurrentPage = articleRowsPage.CurrentPage;
        pageData.Items = shortArticles;
        
        CompleteCategory completeCategory = completeCategoryMapper.makeCompleteCategory(
                categoryRow, 
                pageData);
        
        return completeCategory;
    }
}