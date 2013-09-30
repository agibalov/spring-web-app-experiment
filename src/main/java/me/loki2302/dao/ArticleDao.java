package me.loki2302.dao;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public int createArticle(
            int userId, 
            int categoryId, 
            String title, 
            String text,
            Date createdAt) {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        template.update(
                "insert into Articles(Title, Text, CreatedAt, CategoryId, UserId) " + 
                "values(:title, :text, :createdAt, :categoryId, :userId)",
                new MapSqlParameterSource()                    
                    .addValue("title", title)
                    .addValue("text", text)
                    .addValue("createdAt", createdAt)
                    .addValue("categoryId", categoryId)
                    .addValue("userId", userId),
                keyHolder);
        
        return (Integer)keyHolder.getKey();
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
                "    select Id " + 
                "    from Articles " +
                "    where CategoryId = C.Id " +
                "    order by Id desc limit :take using index) " + 
                "order by A.Id desc", 
                new MapSqlParameterSource()
                    .addValue("take", numberOfRecentArticles)
                    .addValue("categoryIds", categoryIds),
                new ArticleRowMapper());
    }
    
    public List<ArticleRow> getRecentArticlesForCategoriesSlow(
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
                "    order by Id desc) " + 
                "order by A.Id desc", 
                new MapSqlParameterSource()
                    .addValue("take", numberOfRecentArticles)
                    .addValue("categoryIds", categoryIds),
                new ArticleRowMapper());
    }
            
    public Page<ArticleRow> getArticlesByCategory(int categoryId, int itemsPerPage, int page) {
        int numberOfItems = template.queryForObject(
                "select count(Id) from Articles where CategoryId = :categoryId",
                new MapSqlParameterSource()
                    .addValue("categoryId", categoryId),
                Integer.class);
        
        int skip = page * itemsPerPage;
        if(skip >= numberOfItems) {
            throw new RuntimeException("no such page");
        }
        
        List<ArticleRow> items = template.query(
                "select limit :skip :take Id, Title, Text, CreatedAt, UpdatedAt, CategoryId, UserId from Articles where CategoryId = :categoryId", 
                new MapSqlParameterSource()
                    .addValue("categoryId", categoryId)
                    .addValue("skip", skip)
                    .addValue("take", itemsPerPage),
                new ArticleRowMapper());
        
        Page<ArticleRow> pageData = new Page<ArticleRow>();
        pageData.NumberOfItems = numberOfItems;
        pageData.ItemsPerPage = itemsPerPage;
        pageData.NumberOfPages = (numberOfItems / itemsPerPage) + (numberOfItems % itemsPerPage > 0 ? 1 : 0);
        pageData.CurrentPage = page;
        pageData.Items = items;
        return pageData;
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