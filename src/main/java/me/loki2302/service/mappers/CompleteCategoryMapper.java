package me.loki2302.service.mappers;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.Page;
import me.loki2302.service.dto.article.ShortArticle;
import me.loki2302.service.dto.category.CompleteCategory;

import org.springframework.stereotype.Component;

@Component    
public class CompleteCategoryMapper {
    public CompleteCategory makeCompleteCategory(
            CategoryRow categoryRow, 
            Page<ShortArticle> shortArticles) {
        CompleteCategory category = new CompleteCategory();
        category.CategoryId = categoryRow.Id;
        category.Name = categoryRow.Name;
        category.Articles = shortArticles;
        return category;
    }
}