package me.loki2302.service.mappers;

import java.util.Map;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class CompleteArticleMapper {
    public CompleteArticle makeCompleteArticle(
            ArticleRow articleRow, 
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        CompleteArticle completeArticle = new CompleteArticle();
        completeArticle.ArticleId = articleRow.Id;
        completeArticle.Title = articleRow.Title;
        completeArticle.Text = articleRow.Text;
        completeArticle.CreatedAt = articleRow.CreatedAt;
        completeArticle.UpdatedAt = articleRow.UpdatedAt;        
        completeArticle.User = briefUsersMap.get(articleRow.UserId);                        
        completeArticle.Category = briefCategoriesMap.get(articleRow.CategoryId);        
        return completeArticle;
    }
}