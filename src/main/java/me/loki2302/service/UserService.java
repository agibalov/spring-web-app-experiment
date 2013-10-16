package me.loki2302.service;

import me.loki2302.dao.ArticleDao;
import me.loki2302.dao.ArticleVoteDao;
import me.loki2302.dao.CommentDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.ArticleVoteRow;
import me.loki2302.dao.rows.CommentRow;
import me.loki2302.dao.rows.Page;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.ArticleVoteDetails;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.user.CompleteUser;
import me.loki2302.service.exceptions.UserNotFoundException;
import me.loki2302.service.mappers.ArticleMapper;
import me.loki2302.service.mappers.CommentMapper;
import me.loki2302.service.mappers.CompleteUserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private ArticleVoteDao articleVoteDao;
    
    @Autowired
    private ArticleDao articleDao;
    
    @Autowired
    private CommentDao commentDao;
    
    @Autowired
    private CompleteUserMapper completeUserMapper;
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Autowired
    private CommentMapper commentMapper;
        
    public CompleteUser getCompleteUser(int userId) {
        UserRow userRow = userDao.getUser(userId);
        if(userRow == null) {
            throw new UserNotFoundException();
        }
        
        Page<ArticleRow> articleRows = articleDao.getArticlesByUser(userId, 5, 0);
        Page<BriefArticle> briefArticles = articleMapper.makeBriefArticlesPage(articleRows);
        
        Page<CommentRow> commentRows = commentDao.getCommentsByUser(userId, 5, 0);
        Page<Comment> comments = commentMapper.makeCommentsPage(commentRows);
        
        return completeUserMapper.makeCompleteUser(userRow, briefArticles, comments);
    }
    
    public ArticleVoteDetails getArticleVote(Integer userId, int articleId) {
        boolean canVote;
        Integer currentVote = null;
        if(userId != null) {
            canVote = true;
            
            ArticleVoteRow articleVoteRow = articleVoteDao.getUserVote(userId, articleId);
            if(articleVoteRow != null) {
                currentVote = articleVoteRow.Vote;                
            }            
        } else {
            canVote = false;
        }
        
        ArticleVoteDetails articleVoteDetails = new ArticleVoteDetails();
        articleVoteDetails.CanVote = canVote;
        articleVoteDetails.CurrentVote = currentVote;
        
        return articleVoteDetails;
    }
}