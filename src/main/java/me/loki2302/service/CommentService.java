package me.loki2302.service;

import me.loki2302.dao.CommentDao;
import me.loki2302.dao.UserDao;
import me.loki2302.dao.rows.CommentRow;
import me.loki2302.dao.rows.Page;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.exceptions.UserNotFoundException;
import me.loki2302.service.mappers.CommentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CurrentTimeProvider currentTimeProvider;
    
    @Autowired
    private CommentDao commentDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CommentMapper commentMapper;
    
    public int createComment(int userId, int articleId, String text) {
        int commentId = commentDao.createComment(
                userId, 
                articleId, 
                text, 
                currentTimeProvider.getCurrentTime());
        return commentId;
    }
    
    public Page<Comment> getCommentsByUser(int userId, int itemsPerPage, int page) {
        UserRow userRow = userDao.getUser(userId);
        if(userRow == null) {
            throw new UserNotFoundException();
        }
        
        Page<CommentRow> commentRowsPage = commentDao.getCommentsByUser(userId, itemsPerPage, page);
        Page<Comment> commentsPage = commentMapper.makeCommentsPage(commentRowsPage);
        return commentsPage;
    }
}