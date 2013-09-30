package me.loki2302.service;

import me.loki2302.dao.CommentDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CurrentTimeProvider currentTimeProvider;
    
    @Autowired
    private CommentDao commentDao;
    
    public int createComment(int userId, int articleId, String text) {
        int commentId = commentDao.createComment(
                userId, 
                articleId, 
                text, 
                currentTimeProvider.getCurrentTime());
        return commentId;
    }
}