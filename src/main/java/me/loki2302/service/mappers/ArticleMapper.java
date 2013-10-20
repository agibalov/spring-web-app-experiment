package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.Page;
import me.loki2302.service.dto.article.BriefArticle;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.dto.article.ShortArticle;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {
    public Page<BriefArticle> makeBriefArticlesPage(Page<ArticleRow> articleRowsPage) {
        Page<BriefArticle> briefArticlesPage = new Page<BriefArticle>();
        briefArticlesPage.NumberOfItems = articleRowsPage.NumberOfItems;
        briefArticlesPage.ItemsPerPage = articleRowsPage.ItemsPerPage;
        briefArticlesPage.NumberOfPages = articleRowsPage.NumberOfPages;
        briefArticlesPage.CurrentPage = articleRowsPage.CurrentPage;
        briefArticlesPage.Items = makeBriefArticles(articleRowsPage.Items);        
        return briefArticlesPage;
    }
    
    public List<BriefArticle> makeBriefArticles(List<ArticleRow> articleRows) {
        List<BriefArticle> briefArticles = new ArrayList<BriefArticle>(articleRows.size());
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
    
    public List<ShortArticle> makeShortArticles(List<ArticleRow> articleRows) {
        List<ShortArticle> shortArticles = new ArrayList<ShortArticle>(articleRows.size());
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
    
    public CompleteArticle makeCompleteArticle(
            ArticleRow articleRow, 
            List<Comment> comments) {
        CompleteArticle completeArticle = new CompleteArticle();
        completeArticle.ArticleId = articleRow.ArticleId;
        completeArticle.Title = articleRow.Title;
        completeArticle.Text = articleRow.Text;
        completeArticle.CreatedAt = articleRow.CreatedAt;
        completeArticle.UpdatedAt = articleRow.UpdatedAt;
        completeArticle.ReadCount = articleRow.ReadCount;
        completeArticle.CommentCount = articleRow.CommentCount;
        completeArticle.VoteCount = articleRow.VoteCount;
        completeArticle.AverageVote = articleRow.AverageVote;
        completeArticle.User = articleRow.User;
        completeArticle.Category = articleRow.Category;
        completeArticle.Comments = comments;
        return completeArticle;
    }
}