package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.ArticleCommentCountRow;
import me.loki2302.dao.rows.CommentRow;
import me.loki2302.dao.rows.CommentRow2;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Map<Integer, Integer> makeArticleCommentCountMap(List<ArticleCommentCountRow> articleCommentCountRows) {
        Map<Integer, Integer> articleCommentCountMap = new HashMap<Integer, Integer>();
        for(ArticleCommentCountRow articleCommentCountRow : articleCommentCountRows) {
            int articleId = articleCommentCountRow.ArticleId;
            int commentCount = articleCommentCountRow.CommentCount;
            articleCommentCountMap.put(articleId, commentCount);
        }
        return articleCommentCountMap;
    }
    
    public List<Comment> makeComments(
            List<CommentRow> commentRows, 
            Map<Integer, BriefUser> briefUsersMap) {
        
        List<Comment> comments = new ArrayList<Comment>();
        for(CommentRow commentRow : commentRows) {
            Comment comment = makeComment(
                    commentRow, 
                    briefUsersMap);
            comments.add(comment);
        }
        return comments;
    }
    
    public Comment makeComment(
            CommentRow commentRow, 
            Map<Integer, BriefUser> briefUsersMap) {
        Comment comment = new Comment();
        comment.CommentId = commentRow.Id;
        comment.Text = commentRow.Text;
        comment.CreatedAt = commentRow.CreatedAt;
        comment.UpdatedAt = commentRow.UpdatedAt;
        comment.User = briefUsersMap.get(commentRow.UserId);            
        return comment;
    }        
    
    public List<Comment> makeComments2(List<CommentRow2> commentRows) {        
        List<Comment> comments = new ArrayList<Comment>();
        for(CommentRow2 commentRow : commentRows) {
            Comment comment = makeComment2(commentRow);
            comments.add(comment);
        }
        return comments;
    }
    
    public Comment makeComment2(CommentRow2 commentRow) {
        Comment comment = new Comment();
        comment.CommentId = commentRow.CommentId;
        comment.Text = commentRow.Text;
        comment.CreatedAt = commentRow.CreatedAt;
        comment.UpdatedAt = commentRow.UpdatedAt;
        comment.User = commentRow.User;            
        return comment;
    }
}