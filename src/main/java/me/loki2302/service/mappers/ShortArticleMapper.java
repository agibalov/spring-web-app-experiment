package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.service.dto.article.ShortArticle;
import org.springframework.stereotype.Component;

@Component
public class ShortArticleMapper {    
    public List<ShortArticle> makeShortArticles(List<ArticleRow> articleRows) {
        List<ShortArticle> shortArticles = new ArrayList<ShortArticle>();
        for(ArticleRow articleRow : articleRows) {
            ShortArticle shortArticle = makeShortArticle(articleRow);
            shortArticles.add(shortArticle);
        }
        return shortArticles;
    }
        
    public ShortArticle makeShortArticle(ArticleRow articleRow) {
        ShortArticle shortArticle = new ShortArticle();
        shortArticle.ArticleId = articleRow.ArticleId;
        shortArticle.Title = articleRow.Title;
        shortArticle.FirstParagraph = articleRow.Text.split("\n\n")[0];
        shortArticle.CreatedAt = articleRow.CreatedAt;
        shortArticle.UpdatedAt = articleRow.UpdatedAt;
        shortArticle.ReadCount = articleRow.ReadCount;
        shortArticle.CommentCount = articleRow.CommentCount;        
        shortArticle.VoteCount = articleRow.VoteCount;
        shortArticle.AverageVote = articleRow.AverageVote;                
        shortArticle.User = articleRow.User;                        
        shortArticle.Category = articleRow.Category;   
        return shortArticle;
    }
}