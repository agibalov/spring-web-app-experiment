package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class BriefArticleMapper {
    public List<BriefArticle> makeBriefArticles(
            List<ArticleRow> articleRows,
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        List<BriefArticle> briefArticles = new ArrayList<BriefArticle>();
        for(ArticleRow articleRow : articleRows) {
            BriefArticle briefArticle = makeBriefArticle(
                    articleRow,
                    briefUsersMap,
                    briefCategoriesMap);
            briefArticles.add(briefArticle);
        }
        return briefArticles;
    }
    
    public BriefArticle makeBriefArticle(
            ArticleRow articleRow,
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        BriefArticle briefArticle = new BriefArticle();        
        briefArticle.ArticleId = articleRow.Id;
        briefArticle.Title = articleRow.Title;
        briefArticle.CreatedAt = articleRow.CreatedAt;
        briefArticle.UpdatedAt = articleRow.UpdatedAt;
        briefArticle.User = briefUsersMap.get(articleRow.UserId);
        briefArticle.Category = briefCategoriesMap.get(articleRow.CategoryId);        
        return briefArticle;
    }
}