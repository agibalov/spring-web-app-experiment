package me.loki2302.service;

import java.util.Date;
import java.util.List;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.ArticleVoteDao;
import me.loki2302.dao.CategoryDao;
import me.loki2302.dao.CommentDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.CommentRow;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.dto.article.ShortArticle;
import me.loki2302.service.exceptions.AccessToArticleDeniedException;
import me.loki2302.service.exceptions.ArticleNotFoundException;
import me.loki2302.service.mappers.ArticleMapper;
import me.loki2302.service.mappers.CommentMapper;

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
    private ArticleMapper articleMapper;
        
    @Autowired
    private CommentMapper commentMapper;
        
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
    
    public void updateArticle(int userId, int articleId, String title, String text) {
        ArticleRow articleRow = articleDao.getArticle(articleId);
        if(articleRow == null) {
            throw new ArticleNotFoundException();
        }
        
        int articleAuthor = articleRow.User.UserId;
        if(articleAuthor != userId) {
            throw new AccessToArticleDeniedException();
        }
        
        Date currentTime = currentTimeProvider.getCurrentTime();
        articleDao.updateArticle(articleId, title, text, currentTime);
    }
    
    public CompleteArticle getArticle(Integer userId, int articleId) {
        ArticleRow articleRow = articleDao.getArticle(articleId);
        if(articleRow == null) {
            throw new ArticleNotFoundException();
        }
        
        articleDao.increaseArticleReadCount(articleId);
        
        List<CommentRow> commentRows = commentDao.getCommentsByArticleId(articleId);
        List<Comment> comments = commentMapper.makeComments(commentRows);
                
        CompleteArticle completeArticle = articleMapper.makeCompleteArticle(
                articleRow, 
                comments);
                
        return completeArticle;
    }
            
    public List<ShortArticle> getMostRecentArticles(int numberOfMostRecentArticles) {
        List<ArticleRow> articleRows = articleDao.getRecentArticles(numberOfMostRecentArticles);            
        return articleMapper.makeShortArticles(articleRows);
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
