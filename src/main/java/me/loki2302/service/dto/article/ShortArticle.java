package me.loki2302.service.dto.article;

import java.util.Date;

import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

public class ShortArticle {
    public int ArticleId;
    public String Title;
    public String FirstParagraph;
    public Date CreatedAt;
    public Date UpdatedAt;
    public BriefUser User;
    public BriefCategory Category;
}