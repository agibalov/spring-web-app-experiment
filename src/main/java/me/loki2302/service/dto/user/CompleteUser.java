package me.loki2302.service.dto.user;

import me.loki2302.dao.rows.Page;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;

public class CompleteUser {
    public int UserId;
    public String Name;
    public Page<BriefArticle> Articles;
    public Page<Comment> Comments;
}