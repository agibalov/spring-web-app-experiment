package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import me.loki2302.dao.rows.ArticleVoteRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleVoteDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public int getArticleVoteCountByUserId(int articleId, int userId) {
        return template.queryForObject(
                "select count(Id) from ArticleVotes " + 
                "where ArticleId = :articleId and UserId = :userId", 
                new MapSqlParameterSource()
                    .addValue("articleId", articleId)
                    .addValue("userId", userId),
                Integer.class);
    }
        
    public void insertArticleVoteCount(int articleId, int userId, int vote, Date createdAt) {
        template.update(
                "insert into ArticleVotes(CreatedAt, UpdatedAt, Vote, ArticleId, UserId) " + 
                "values(:createdAt, :updatedAt, :vote, :articleId, :userId)", 
                new MapSqlParameterSource()
                    .addValue("articleId", articleId)
                    .addValue("userId", userId)
                    .addValue("vote", vote)
                    .addValue("createdAt", createdAt)
                    .addValue("updatedAt", null));
    }
    
    public void updateArticleVoteCount(int articleId, int userId, int vote, Date updatedAt) {
        template.update(
                "update ArticleVotes set Vote = :vote, UpdatedAt = :updatedAt " + 
                "where ArticleId = :articleId and UserId = :userId", 
                new MapSqlParameterSource()
                    .addValue("articleId", articleId)
                    .addValue("userId", userId)
                    .addValue("vote", vote)                    
                    .addValue("updatedAt", updatedAt));
    }
                
    public ArticleVoteRow getUserVote(int userId, int articleId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, CreatedAt, UpdatedAt, Vote, ArticleId, UserId " + 
                "from ArticleVotes " +
                "where ArticleId = :articleId and UserId = :userId",
                new MapSqlParameterSource()
                    .addValue("articleId", articleId)
                    .addValue("userId", userId),
                new ArticleVoteRowMapper()));
    }
            
    private static class ArticleVoteRowMapper implements RowMapper<ArticleVoteRow> {
        @Override
        public ArticleVoteRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleVoteRow articleVoteRow = new ArticleVoteRow();
            articleVoteRow.Id = rs.getInt("Id");
            articleVoteRow.CreatedAt = rs.getTimestamp("CreatedAt");
            articleVoteRow.UpdatedAt = rs.getTimestamp("UpdatedAt");
            articleVoteRow.Vote = rs.getInt("Vote");
            articleVoteRow.ArticleId = rs.getInt("ArticleId");
            articleVoteRow.UserId = rs.getInt("UserId");
            return articleVoteRow;
        }        
    }
}