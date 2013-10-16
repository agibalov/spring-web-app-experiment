package me.loki2302.service.mappers;

import java.util.List;

import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.user.CompleteUser;

import org.springframework.stereotype.Component;

@Component
public class CompleteUserMapper {
    public CompleteUser makeCompleteUser(
            UserRow userRow, 
            List<BriefArticle> articles, 
            List<Comment> comments) {
        
        CompleteUser user = new CompleteUser();
        user.UserId = userRow.Id;
        user.Name = userRow.Name;
        user.Articles = articles;
        user.Comments = comments;
        
        return user;
    }
}