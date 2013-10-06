package me.loki2302.dao.rows;

import java.util.Date;

import me.loki2302.service.dto.user.BriefUser;

public class CommentRow {
    public int CommentId;
    public String Text;
    public Date CreatedAt;
    public Date UpdatedAt;
    public BriefUser User;
}