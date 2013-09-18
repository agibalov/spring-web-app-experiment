package me.loki2302.service.dto;

import java.util.List;

import me.loki2302.service.dto.article.ShortArticle;
import me.loki2302.service.dto.category.BriefCategory;

public class Home {
    public List<BriefCategory> Categories;
    public List<ShortArticle> MostRecentArticles;
}