package me.loki2302.dao;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.loki2302.dao.rows.ArticleRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public ArticleRow createArticle(
            final int userId, 
            final int categoryId, 
            final String title, 
            final String text,
            final Date createdAt) {
        
        final String rowUuid = UUID.randomUUID().toString();                
        template.update(
                "insert into Articles(RowUuid, Title, Text, CreatedAt, CategoryId, UserId) " + 
                "values(:rowUuid, :title, :text, :createdAt, :categoryId, :userId)",
                new HashMap<String, Object>() {{
                    put("rowUuid", rowUuid);
                    put("title", title);
                    put("text", text);
                    put("createdAt", createdAt);
                    put("categoryId", categoryId);
                    put("userId", userId);
                }});
        
        ArticleRow articleRow = template.queryForObject(
                "select Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where RowUuid = :rowUuid",
                new HashMap<String, Object>() {{
                    put("rowUuid", rowUuid);
                }},
                new ArticleRowMapper());
                
        return articleRow;
    }
    
    public ArticleRow getArticle(final int articleId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where Id = :articleId",
                new HashMap<String, Object>() {{
                    put("articleId", articleId);
                }},
                new ArticleRowMapper()));
    }
        
    public List<ArticleRow> getRecentArticles(final int numberOfArticles) {
        return template.query(
                "select top :take Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId " + 
                "from Articles " + 
                "order by CreatedAt desc", 
                new HashMap<String, Object>() {{
                    put("take", numberOfArticles);
                }},
                new ArticleRowMapper());
    }
        
    public List<ArticleRow> getArticlesByCategory(final int categoryId) {
        return template.query(
                "select Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where CategoryId = :categoryId", 
                new HashMap<String, Object>() {{
                    put("categoryId", categoryId);
                }},
                new ArticleRowMapper());
    }
    
    private static class ArticleRowMapper implements RowMapper<ArticleRow> {
        @Override
        public ArticleRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleRow articleRow = new ArticleRow();
            articleRow.Id = rs.getInt("Id");
            articleRow.Title = rs.getString("Title");
            articleRow.Text = rs.getString("Text");
            articleRow.CreatedAt = rs.getTimestamp("CreatedAt");
            articleRow.UpdatedAt = rs.getTimestamp("UpdatedAt");
            articleRow.CategoryId = rs.getInt("CategoryId");
            articleRow.UserId = rs.getInt("UserId");
            return articleRow;
        }        
    }
}