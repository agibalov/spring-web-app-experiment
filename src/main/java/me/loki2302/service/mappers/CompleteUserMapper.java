package me.loki2302.service.mappers;

import me.loki2302.dao.rows.Page;
import me.loki2302.dao.rows.UserRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.user.CompleteUser;

import org.springframework.stereotype.Component;

@Component
public class CompleteUserMapper {
    public CompleteUser makeCompleteUser(
            UserRow userRow, 
            Page<BriefArticle> articles, 
            Page<Comment> comments) {
        
        CompleteUser user = new CompleteUser();
        user.UserId = userRow.Id;
        user.Name = userRow.Name;
        user.Articles = articles;
        user.Comments = comments;
        
        return user;
    }
}