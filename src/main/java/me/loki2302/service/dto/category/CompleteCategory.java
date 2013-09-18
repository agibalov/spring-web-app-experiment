package me.loki2302.service.dto.category;

import java.util.List;

import me.loki2302.service.dto.article.ShortArticle;

public class CompleteCategory {
    public int CategoryId;
    public String Name;
    public List<ShortArticle> Articles;
}