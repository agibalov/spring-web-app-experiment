package me.loki2302.dao;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import me.loki2302.dao.rows.ArticleRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public ArticleRow createArticle(
            int userId, 
            int categoryId, 
            String title, 
            String text,
            Date createdAt) {
        
        String rowUuid = UUID.randomUUID().toString();                
        template.update(
                "insert into Articles(RowUuid, Title, Text, CreatedAt, CategoryId, UserId) " + 
                "values(:rowUuid, :title, :text, :createdAt, :categoryId, :userId)",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid)
                    .addValue("title", title)
                    .addValue("text", text)
                    .addValue("createdAt", createdAt)
                    .addValue("categoryId", categoryId)
                    .addValue("userId", userId));
        
        ArticleRow articleRow = template.queryForObject(
                "select Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where RowUuid = :rowUuid",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid),
                new ArticleRowMapper());
                
        return articleRow;
    }
    
    public ArticleRow getArticle(int articleId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where Id = :articleId",
                new MapSqlParameterSource()
                    .addValue("articleId", articleId),                
                new ArticleRowMapper()));
    }
        
    public List<ArticleRow> getRecentArticles(int numberOfArticles) {
        return template.query(
                "select top :take Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId " + 
                "from Articles " + 
                "order by Id desc", 
                new MapSqlParameterSource()
                    .addValue("take", numberOfArticles),
                new ArticleRowMapper());
    }
    
    public List<ArticleRow> getRecentArticlesForCategories(
            Iterable<Integer> categoryIds, 
            int numberOfRecentArticles) {
        return template.query(
                "select A.Id, A.Title, A.Text, A.CreatedAt, A.UpdatedAt, A.CategoryId, A.UserId " +
                "from Categories as C " +
                "join Articles as A on A.CategoryId = C.Id " +
                "where C.Id in (:categoryIds) and A.Id in ( " +
                "    select top :take Id " + 
                "    from Articles " +
                "    where CategoryId = C.Id " +
                "    order by Id desc)", 
                new MapSqlParameterSource()
                    .addValue("take", numberOfRecentArticles)
                    .addValue("categoryIds", categoryIds),
                new ArticleRowMapper());
    }
        
    public List<ArticleRow> getArticlesByCategory(int categoryId) {
        return template.query(
                "select Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where CategoryId = :categoryId", 
                new MapSqlParameterSource()
                    .addValue("categoryId", categoryId),
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