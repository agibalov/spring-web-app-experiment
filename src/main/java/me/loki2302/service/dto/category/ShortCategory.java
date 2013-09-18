package me.loki2302.service.dto.category;

import java.util.List;

import me.loki2302.service.dto.article.BriefArticle;

public class ShortCategory {
    public int CategoryId;
    public String Name;
    public List<BriefArticle> RecentArticles;
}