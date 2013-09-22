package me.loki2302.service.mappers;

import java.util.HashSet;
import java.util.Set;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.CategoryRow;

public abstract class MappingHelpers {    
    public static Set<Integer> extractUserIds(Iterable<ArticleRow> articleRows) {
        Set<Integer> userIds = new HashSet<Integer>();
        for(ArticleRow articleRow : articleRows) {
            userIds.add(articleRow.UserId);
        }
        return userIds;
    }
    
    public static Set<Integer> extractCategoryIds(Iterable<ArticleRow> articleRows) {
        Set<Integer> userIds = new HashSet<Integer>();
        for(ArticleRow articleRow : articleRows) {
            userIds.add(articleRow.CategoryId);
        }
        return userIds;
    }
    
    public static Set<Integer> extractIds(Iterable<CategoryRow> categoryRows) {
        Set<Integer> categoryIds = new HashSet<Integer>();
        for(CategoryRow categoryRow : categoryRows) {
            categoryIds.add(categoryRow.Id);
        }
        return categoryIds;
    }
}