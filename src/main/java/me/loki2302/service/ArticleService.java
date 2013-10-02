package me.loki2302.service;

import java.util.Date;
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
import me.loki2302.dao.rows.ArticleVoteRow;
import me.loki2302.dao.rows.ArticleVoteStatsRow;
import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.CommentRow;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.dto.article.ShortArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;
import me.loki2302.service.exceptions.ArticleNotFoundException;
import me.loki2302.service.mappers.ArticleVoteStatsMapper;
import me.loki2302.service.mappers.BriefCategoryMapper;
import me.loki2302.service.mappers.BriefUserMapper;
import me.loki2302.service.mappers.CommentMapper;
import me.loki2302.service.mappers.CompleteArticleMapper;
import me.loki2302.service.mappers.MappingHelpers;
import me.loki2302.service.mappers.ShortArticleMapper;

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
    private CommentDao commentDao;
    
    @Autowired
    private ArticleVoteDao articleVoteDao;
    
    @Autowired
    private BriefUserMapper briefUserMapper;
    
    @Autowired
    private BriefCategoryMapper briefCategoryMapper;
    
    @Autowired
    private CompleteArticleMapper completeArticleMapper;
    
    @Autowired
    private ShortArticleMapper shortArticleMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private ArticleVoteStatsMapper articleVoteStatsMapper;
    
    public int createArticle(int userId, int categoryId, String title, String text) {
        Date currentTime = currentTimeProvider.getCurrentTime();
        int articleId = articleDao.createArticle(
                userId, 
                categoryId, 
                title, 
                text, 
                currentTime);
        
        return articleId; 
    }
        
    public CompleteArticle getArticle(Integer userId, int articleId) {
        ArticleRow articleRow = articleDao.getArticle(articleId);
        if(articleRow == null) {
            throw new ArticleNotFoundException();
        }
        
        articleDao.increaseArticleReadCount(articleId);
        
        UserRow authorUserRow = userDao.getUser(articleRow.UserId);
        BriefUser authorBriefUser = briefUserMapper.makeBriefUser(authorUserRow);
        
        CategoryRow categoryRow = categoryDao.getCategory(articleRow.CategoryId);
        BriefCategory briefCategory = briefCategoryMapper.makeBriefCategory(categoryRow);
        
        List<CommentRow> commentRows = commentDao.getCommentsByArticleId(articleId);
        Set<Integer> commentUserIds = MappingHelpers.extractUserIdsFromCommentRows(commentRows);
        List<UserRow> commentUserRows = userDao.getUsers(commentUserIds);
        Map<Integer, BriefUser> commentBriefUsersMap = briefUserMapper.makeBriefUsersMap(commentUserRows);
        List<Comment> comments = commentMapper.makeComments(commentRows, commentBriefUsersMap);
        
        ArticleVoteStatsRow articleVoteStatsRow = articleVoteDao.getVoteStatsByArticleId(articleId);
        
        if(userId != null) {
            ArticleVoteRow articleVoteRow = articleVoteDao.getUserVote(userId, articleId);
            if(articleVoteRow != null) {
                // TODO: can vote
                // TODO: already voted - articleVoteRow.Vote                
            } else {
                // TODO: can vote
            }            
        } else {
            // TODO: not authenticated, can't vote
        }
        
        CompleteArticle completeArticle = completeArticleMapper.makeCompleteArticle(
                articleRow, 
                articleVoteStatsRow,
                authorBriefUser,
                briefCategory,
                comments);
                
        return completeArticle;
    }
    
    public List<ShortArticle> getMostRecentArticles(int numberOfMostRecentArticles) {
        List<ArticleRow> articleRows = articleDao.getRecentArticles(numberOfMostRecentArticles);
        
        Set<Integer> articleIds = MappingHelpers.extractArticleIdsFromArticleRows(articleRows);
        
        List<ArticleCommentCountRow> articleCommentCountRows = commentDao.getCommentCountsByArticleIds(articleIds);
        Map<Integer, Integer> articleCommentCountsMap = commentMapper.makeArticleCommentCountMap(articleCommentCountRows);
        
        List<ArticleVoteStatsRow> articleVoteStatsRows = articleVoteDao.getVoteStatsByArticleIds(articleIds);
        Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap = articleVoteStatsMapper.makeArticleVoteStatsMap(articleVoteStatsRows);
        
        Set<Integer> userIds = MappingHelpers.extractUserIdsFromArticleRows(articleRows);
        List<UserRow> userRows = userDao.getUsers(userIds);
        Map<Integer, BriefUser> briefUsersMap = briefUserMapper.makeBriefUsersMap(userRows);
            
        Set<Integer> categoryIds = MappingHelpers.extractCategoryIdsFromArticleRows(articleRows);
        List<CategoryRow> categoryRows = categoryDao.getCategories(categoryIds);
        Map<Integer, BriefCategory> briefCategoriesMap = briefCategoryMapper.makeBriefCategoriesMap(categoryRows);
            
        return shortArticleMapper.makeShortArticles(
                articleRows,                
                articleCommentCountsMap,
                articleVoteStatsMap,
                briefUsersMap, 
                briefCategoriesMap);
    }
    
    public void voteForArticle(int userId, int articleId, int vote) {
        if(vote < 1 || vote > 5) {
            throw new RuntimeException();
        }
        
        Date currentTime = currentTimeProvider.getCurrentTime();
        int articleVoteCount = articleVoteDao.getArticleVoteCountByUserId(articleId, userId);
        if(articleVoteCount == 0) {
            articleVoteDao.insertArticleVoteCount(articleId, userId, vote, currentTime);
        } else {
            articleVoteDao.updateArticleVoteCount(articleId, userId, vote, currentTime);
        }
    }
}
