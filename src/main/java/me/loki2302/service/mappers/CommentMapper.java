package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;

import me.loki2302.dao.rows.CommentRow;
import me.loki2302.dao.rows.Page;
import me.loki2302.service.dto.article.Comment;

import org.springframework.stereotype.Component;

@Component
public class CommentMapper {        
    public Page<Comment> makeCommentsPage(Page<CommentRow> commentRowsPage) {
        Page<Comment> commentsPage = new Page<Comment>();
        commentsPage.NumberOfItems = commentRowsPage.NumberOfItems;
        commentsPage.ItemsPerPage = commentRowsPage.ItemsPerPage;
        commentsPage.NumberOfPages = commentRowsPage.NumberOfPages;
        commentsPage.CurrentPage = commentRowsPage.CurrentPage;
        commentsPage.Items = makeComments(commentRowsPage.Items);        
        return commentsPage;
    }
    
    public List<Comment> makeComments(List<CommentRow> commentRows) {        
        List<Comment> comments = new ArrayList<Comment>();
        for(CommentRow commentRow : commentRows) {
            Comment comment = makeComment(commentRow);
            comments.add(comment);
        }
        return comments;
    }
    
    public Comment makeComment(CommentRow commentRow) {
        Comment comment = new Comment();
        comment.CommentId = commentRow.CommentId;
        comment.Text = commentRow.Text;
        comment.CreatedAt = commentRow.CreatedAt;
        comment.UpdatedAt = commentRow.UpdatedAt;
        comment.User = commentRow.User;            
        return comment;
    }
}