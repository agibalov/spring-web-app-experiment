package me.loki2302.service.dto.article;

import java.util.Date;

import me.loki2302.service.dto.user.BriefUser;

public class Comment {
    public int CommentId;
    public String Text;
    public Date CreatedAt;
    public Date UpdatedAt;
    public BriefUser User;
}