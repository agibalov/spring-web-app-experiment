package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import me.loki2302.dao.rows.ArticleVoteStatsRow;
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
        
    public ArticleVoteStatsRow getVoteStatsByArticleId(int articleId) {        
        return DataAccessUtils.singleResult(template.query(
                "select A.Id as ArticleId, count(V.Id) as VoteCount, avg(V.Vote) as AverageVote " + 
                "from Articles as A " +
                "left join ArticleVotes as V on V.ArticleId = A.Id " +
                "where A.Id = :articleId " +
                "group by A.Id", 
                new MapSqlParameterSource()
                    .addValue("articleId", articleId),
                new ArticleVoteStatsRowMapper()));
    }
    
    public List<ArticleVoteStatsRow> getVoteStatsByArticleIds(Collection<Integer> articleIds) {
        if(articleIds.isEmpty()) {
            return new ArrayList<ArticleVoteStatsRow>();
        }
        
        return template.query(
                "select A.Id as ArticleId, count(V.Id) as VoteCount, avg(V.Vote) as AverageVote " + 
                "from Articles as A " +
                "left join ArticleVotes as V on V.ArticleId = A.Id " +
                "where A.Id in (:articleIds) " +
                "group by A.Id", 
                new MapSqlParameterSource()
                    .addValue("articleIds", articleIds),
                new ArticleVoteStatsRowMapper());
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
        
    private static class ArticleVoteStatsRowMapper implements RowMapper<ArticleVoteStatsRow> {
        @Override
        public ArticleVoteStatsRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleVoteStatsRow articleVoteStatsRow = new ArticleVoteStatsRow();
            articleVoteStatsRow.ArticleId = rs.getInt("ArticleId");
            articleVoteStatsRow.VoteCount = rs.getInt("VoteCount");
            articleVoteStatsRow.AverageVote = rs.getInt("AverageVote");
            return articleVoteStatsRow;
        }        
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