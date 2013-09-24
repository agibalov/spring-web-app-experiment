package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.category.ShortCategory;

import org.springframework.stereotype.Component;

@Component
public class ShortCategoryMapper {
    public ShortCategory makeShortCategory(
            CategoryRow categoryRow, 
            List<BriefArticle> recentArticles) {
        ShortCategory shortCategory = new ShortCategory();
        shortCategory.CategoryId = categoryRow.Id;
        shortCategory.Name = categoryRow.Name;
        shortCategory.RecentArticles = recentArticles;                
        return shortCategory;
    }
    
    public List<ShortCategory> makeShortCategories(
            List<CategoryRow> categoryRows, 
            List<BriefArticle> briefArticles) {
        
        Map<Integer, List<BriefArticle>> briefArticlesByCategoryIds = new HashMap<Integer, List<BriefArticle>>();
        for(BriefArticle briefArticle : briefArticles) {
            int categoryId = briefArticle.Category.CategoryId;
            if(!briefArticlesByCategoryIds.containsKey(categoryId)) {
                briefArticlesByCategoryIds.put(categoryId, new ArrayList<BriefArticle>());
            }
            List<BriefArticle> briefArticlesForCategory = briefArticlesByCategoryIds.get(categoryId);
            briefArticlesForCategory.add(briefArticle);
        }
        
        List<ShortCategory> shortCategories = new ArrayList<ShortCategory>();
        for(CategoryRow categoryRow : categoryRows) {            
            List<BriefArticle> briefArticlesForThisCategory = briefArticlesByCategoryIds.get(categoryRow.Id);
            
            ShortCategory shortCategory = makeShortCategory(
                    categoryRow, 
                    briefArticlesForThisCategory);
            
            shortCategories.add(shortCategory);
        }
        
        return shortCategories;
    }
}