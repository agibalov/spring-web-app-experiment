package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import me.loki2302.dao.rows.CommentRow;
import me.loki2302.service.dto.user.BriefUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public int createComment(int userId, int articleId, String text, Date createdAt) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        template.update(
                "insert into Comments(Text, CreatedAt, ArticleId, UserId) " + 
                "values(:text, :createdAt, :articleId, :userId)", 
                new MapSqlParameterSource()
                    .addValue("text", text)
                    .addValue("createdAt", createdAt)
                    .addValue("articleId", articleId)
                    .addValue("userId", userId),
                keyHolder);
        
        return (Integer)keyHolder.getKey();
    }
            
    public List<CommentRow> getCommentsByArticleId(int articleId) {
        return template.query(
                "select " + 
                "C.Id as Id, C.Text as Text, C.CreatedAt as CreatedAt, C.UpdatedAt as UpdatedAt, " + 
                "U.Id as UserId, U.Name as UserName " + 
                "from Comments as C " +
                "join Users as U on U.Id = C.UserId " +
                "where C.ArticleId = :articleId " +
                "order by C.Id desc", 
                new MapSqlParameterSource()
                    .addValue("articleId", articleId),
                new CommentRowMapper());
    }
    
    public List<CommentRow> getCommentsByUser(int userId) {
        return template.query(
                "select " + 
                "C.Id as Id, C.Text as Text, C.CreatedAt as CreatedAt, C.UpdatedAt as UpdatedAt, " + 
                "U.Id as UserId, U.Name as UserName " + 
                "from Comments as C " +
                "join Users as U on U.Id = C.UserId " +
                "where U.Id = :userId " +
                "order by C.Id desc", 
                new MapSqlParameterSource()
                    .addValue("userId", userId),
                new CommentRowMapper());
    }
                
    private static class CommentRowMapper implements RowMapper<CommentRow> {
        @Override
        public CommentRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentRow commentRow = new CommentRow();
            commentRow.CommentId = rs.getInt("Id");
            commentRow.Text = rs.getString("Text");
            commentRow.CreatedAt = rs.getTimestamp("CreatedAt");
            commentRow.UpdatedAt = rs.getTimestamp("UpdatedAt");
            
            BriefUser briefUser = new BriefUser();
            briefUser.UserId = rs.getInt("UserId");
            briefUser.Name = rs.getString("UserName");            
            commentRow.User = briefUser;
            
            return commentRow;
        }        
    }
}