package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.ArticleVoteStatsRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class BriefArticleMapper {
    public List<BriefArticle> makeBriefArticles(
            List<ArticleRow> articleRows,
            Map<Integer, Integer> commentCountsMap,
            Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap,
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        List<BriefArticle> briefArticles = new ArrayList<BriefArticle>();
        for(ArticleRow articleRow : articleRows) {
            BriefArticle briefArticle = makeBriefArticle(
                    articleRow,
                    commentCountsMap,
                    articleVoteStatsMap,
                    briefUsersMap,
                    briefCategoriesMap);
            briefArticles.add(briefArticle);
        }
        return briefArticles;
    }
    
    public BriefArticle makeBriefArticle(
            ArticleRow articleRow,
            Map<Integer, Integer> commentCountsMap,
            Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap,
            Map<Integer, BriefUser> briefUsersMap, 
            Map<Integer, BriefCategory> briefCategoriesMap) {
        BriefArticle briefArticle = new BriefArticle();        
        briefArticle.ArticleId = articleRow.Id;
        briefArticle.Title = articleRow.Title;
        briefArticle.CreatedAt = articleRow.CreatedAt;
        briefArticle.UpdatedAt = articleRow.UpdatedAt;
        briefArticle.ReadCount = articleRow.ReadCount;
        briefArticle.CommentCount = commentCountsMap.get(articleRow.Id);
        briefArticle.VoteCount = articleVoteStatsMap.get(articleRow.Id).VoteCount;
        briefArticle.AverageVote = articleVoteStatsMap.get(articleRow.Id).AverageVote;
        briefArticle.User = briefUsersMap.get(articleRow.UserId);
        briefArticle.Category = briefCategoriesMap.get(articleRow.CategoryId);        
        return briefArticle;
    }
}