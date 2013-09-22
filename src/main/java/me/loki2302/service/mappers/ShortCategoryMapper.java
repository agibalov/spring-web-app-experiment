package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;

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
        List<ShortCategory> shortCategories = new ArrayList<ShortCategory>();
        for(CategoryRow categoryRow : categoryRows) {
            //
            List<BriefArticle> briefArticlesForThisCategory = new ArrayList<BriefArticle>();
            for(BriefArticle briefArticle : briefArticles) {
                if(briefArticle.Category.CategoryId != categoryRow.Id) {
                    continue;
                }
                
                briefArticlesForThisCategory.add(briefArticle);
            }
            //
            
            ShortCategory shortCategory = makeShortCategory(
                    categoryRow, 
                    briefArticlesForThisCategory);
            
            shortCategories.add(shortCategory);
        }
        
        return shortCategories;
    }
}