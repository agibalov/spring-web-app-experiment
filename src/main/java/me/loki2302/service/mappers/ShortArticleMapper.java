package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.ArticleVoteStatsRow;
import me.loki2302.service.dto.article.ShortArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class ShortArticleMapper {
    public List<ShortArticle> makeShortArticles(
            List<ArticleRow> articleRows,
            Map<Integer, Integer> commentCountsMap,
            Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap,
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        List<ShortArticle> shortArticles = new ArrayList<ShortArticle>();
        for(ArticleRow articleRow : articleRows) {
            ShortArticle shortArticle = makeShortArticle(
                    articleRow,
                    commentCountsMap,
                    articleVoteStatsMap,
                    briefUsersMap,
                    briefCategoriesMap);
            shortArticles.add(shortArticle);
        }
        return shortArticles;
    }
    
    public ShortArticle makeShortArticle(
            ArticleRow articleRow,            
            Map<Integer, Integer> commentCountsMap,
            Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap,
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        ShortArticle shortArticle = new ShortArticle();
        shortArticle.ArticleId = articleRow.Id;
        shortArticle.Title = articleRow.Title;
        shortArticle.FirstParagraph = articleRow.Text.split("\n\n")[0];
        shortArticle.CreatedAt = articleRow.CreatedAt;
        shortArticle.UpdatedAt = articleRow.UpdatedAt;
        shortArticle.ReadCount = articleRow.ReadCount;
        shortArticle.CommentCount = commentCountsMap.get(articleRow.Id);        
        shortArticle.VoteCount = articleVoteStatsMap.get(articleRow.Id).VoteCount;
        shortArticle.AverageVote = articleVoteStatsMap.get(articleRow.Id).AverageVote;                
        shortArticle.User = briefUsersMap.get(articleRow.UserId);                        
        shortArticle.Category = briefCategoriesMap.get(articleRow.CategoryId);   
        return shortArticle;
    }
}