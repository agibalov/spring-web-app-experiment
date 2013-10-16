package me.loki2302.service.dto.user;

import java.util.List;

import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;

public class CompleteUser {
    public int UserId;
    public String Name;
    public List<BriefArticle> Articles;
    public List<Comment> Comments;
}