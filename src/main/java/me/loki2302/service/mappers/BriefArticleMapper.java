package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.ShortArticle;
import org.springframework.stereotype.Component;

@Component
public class BriefArticleMapper {    
    public List<BriefArticle> makeBriefArticles(List<ArticleRow> articleRows) {
        List<BriefArticle> briefArticles = new ArrayList<BriefArticle>();
        for(ArticleRow articleRow : articleRows) {
            BriefArticle briefArticle = makeBriefArticle(
                    articleRow);
            briefArticles.add(briefArticle);
        }
        return briefArticles;
    }
        
    public BriefArticle makeBriefArticle(ArticleRow articleRow) {
        BriefArticle briefArticle = new ShortArticle();
        briefArticle.ArticleId = articleRow.ArticleId;
        briefArticle.Title = articleRow.Title;
        briefArticle.CreatedAt = articleRow.CreatedAt;
        briefArticle.UpdatedAt = articleRow.UpdatedAt;
        briefArticle.ReadCount = articleRow.ReadCount;
        briefArticle.CommentCount = articleRow.CommentCount;        
        briefArticle.VoteCount = articleRow.VoteCount;
        briefArticle.AverageVote = articleRow.AverageVote;                
        briefArticle.User = articleRow.User;                        
        briefArticle.Category = articleRow.Category;   
        return briefArticle;
    }
}