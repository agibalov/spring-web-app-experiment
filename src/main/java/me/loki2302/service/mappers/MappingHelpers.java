package me.loki2302.service.mappers;

import java.util.HashSet;
import java.util.Set;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.CommentRow;

public abstract class MappingHelpers {    
    public static Set<Integer> extractUserIdsFromArticleRows(Iterable<ArticleRow> articleRows) {
        Set<Integer> userIds = new HashSet<Integer>();
        for(ArticleRow articleRow : articleRows) {
            userIds.add(articleRow.UserId);
        }
        return userIds;
    }
    
    public static Set<Integer> extractUserIdsFromCommentRows(Iterable<CommentRow> commentRows) {
        Set<Integer> userIds = new HashSet<Integer>();
        for(CommentRow commentRow : commentRows) {
            userIds.add(commentRow.UserId);
        }
        return userIds;
    }
    
    public static Set<Integer> extractCategoryIdsFromArticleRows(Iterable<ArticleRow> articleRows) {
        Set<Integer> userIds = new HashSet<Integer>();
        for(ArticleRow articleRow : articleRows) {
            userIds.add(articleRow.CategoryId);
        }
        return userIds;
    }
    
    public static Set<Integer> extractCategoryIdsFromCategoryRows(Iterable<CategoryRow> categoryRows) {
        Set<Integer> categoryIds = new HashSet<Integer>();
        for(CategoryRow categoryRow : categoryRows) {
            categoryIds.add(categoryRow.Id);
        }
        return categoryIds;
    }
    
    public static Set<Integer> extractArticleIdsFromArticleRows(Iterable<ArticleRow> articleRows) {
        Set<Integer> articleIds = new HashSet<Integer>();
        for(ArticleRow articleRow : articleRows) {
            articleIds.add(articleRow.Id);
        }
        return articleIds;
    }
}