package me.loki2302.service;

import me.loki2302.dao.ArticleVoteDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.ArticleVoteRow;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.ArticleVoteDetails;
import me.loki2302.service.dto.user.CompleteUser;
import me.loki2302.service.exceptions.UserNotFoundException;
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
    private CompleteUserMapper completeUserMapper;
        
    public CompleteUser getCompleteUser(int userId) {
        UserRow userRow = userDao.getUser(userId);
        if(userRow == null) {
            throw new UserNotFoundException();
        }
        
        return completeUserMapper.makeCompleteUser(userRow);
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