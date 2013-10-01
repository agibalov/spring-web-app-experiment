package me.loki2302.service.mappers;

import java.util.List;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.ArticleVoteStatsRow;
import me.loki2302.service.dto.article.Comment;
import me.loki2302.service.dto.article.CompleteArticle;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.stereotype.Component;

@Component
public class CompleteArticleMapper {
    public CompleteArticle makeCompleteArticle(
            ArticleRow articleRow, 
            ArticleVoteStatsRow articleVoteStatsRow,
            BriefUser author, 
            BriefCategory category,
            List<Comment> comments) {
        CompleteArticle completeArticle = new CompleteArticle();
        completeArticle.ArticleId = articleRow.Id;
        completeArticle.Title = articleRow.Title;
        completeArticle.Text = articleRow.Text;
        completeArticle.CreatedAt = articleRow.CreatedAt;
        completeArticle.UpdatedAt = articleRow.UpdatedAt;
        completeArticle.ReadCount = articleRow.ReadCount;
        completeArticle.CommentCount = comments.size();
        completeArticle.VoteCount = articleVoteStatsRow.VoteCount;
        completeArticle.AverageVote = articleVoteStatsRow.AverageVote;
        completeArticle.User = author;
        completeArticle.Category = category;
        completeArticle.Comments = comments;
        return completeArticle;
    }
}