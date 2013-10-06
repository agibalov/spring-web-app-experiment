package me.loki2302.dao.rows;

import java.util.Date;

import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

public class ArticleRow {
    public int ArticleId;
    public String Title;
    public String Text;
    public Date CreatedAt;
    public Date UpdatedAt;
    public int ReadCount;
    public int CommentCount;
    public int VoteCount;
    public int AverageVote;
    public BriefUser User;
    public BriefCategory Category;        
}