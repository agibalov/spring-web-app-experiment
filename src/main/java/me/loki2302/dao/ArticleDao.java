package me.loki2302.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import me.loki2302.dao.rows.ArticleRow;
import me.loki2302.dao.rows.ArticleRow2;
import me.loki2302.dao.rows.Page;
import me.loki2302.service.dto.category.BriefCategory;
import me.loki2302.service.dto.user.BriefUser;

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
                "insert into Articles(Title, Text, CreatedAt, ReadCount, CategoryId, UserId) " + 
                "values(:title, :text, :createdAt, :readCount, :categoryId, :userId)",
                new MapSqlParameterSource()                    
                    .addValue("title", title)
                    .addValue("text", text)
                    .addValue("createdAt", createdAt)
                    .addValue("readCount", 0)
                    .addValue("categoryId", categoryId)
                    .addValue("userId", userId),
                keyHolder);
        
        return (Integer)keyHolder.getKey();
    }
    
    public ArticleRow getArticle(int articleId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Title, Text, CreatedAt, UpdatedAt, ReadCount, CategoryId, UserId " + 
                "from Articles where Id = :articleId",
                new MapSqlParameterSource()
                    .addValue("articleId", articleId),                
                new ArticleRowMapper()));
    }

    public ArticleRow2 getArticle2(int articleId) {
        return DataAccessUtils.singleResult(template.query(
                "select " + 
                "  A.Id as Id, A.Title as Title, A.Text as Text, A.CreatedAt as CreatedAt, A.UpdatedAt as UpdatedAt, A.ReadCount as ReadCount, " +
                "  count(Comment.Id) as CommentCount, " + 
                "  count(V.Id) as VoteCount, avg(V.Vote) as AverageVote, " +
                "  U.Id as UserId, U.Name as UserName, " + 
                "  Category.Id as CategoryId, Category.Name as CategoryName " + 
                "from Articles as A " +
                "join Users as U on U.Id = A.UserId " +
                "join Categories as Category on Category.Id = A.CategoryId " +
                "left join Comments as Comment on Comment.ArticleId = A.Id " +
                "left join ArticleVotes as V on V.ArticleId = A.Id " +
                "where A.Id = :articleId " +
                "group by A.Id, A.Title, A.Text, A.CreatedAt, A.UpdatedAt, A.ReadCount, U.Id, U.Name, Category.Id, Category.Name",
                new MapSqlParameterSource()
                    .addValue("articleId", articleId),                
                new ArticleRow2Mapper()));
    }    
    
    public void increaseArticleReadCount(int articleId) {
        template.update(
                "update Articles set ReadCount = ReadCount + 1 where Id = :articleId", 
                new MapSqlParameterSource()
                    .addValue("articleId", articleId));
    }
        
    public List<ArticleRow> getRecentArticles(int numberOfArticles) {
        return template.query(
                "select top :take Id, Title, Text, CreatedAt, UpdatedAt, ReadCount, CategoryId, UserId " + 
                "from Articles " + 
                "order by Id desc", 
                new MapSqlParameterSource()
                    .addValue("take", numberOfArticles),
                new ArticleRowMapper());
    }
    
    public List<ArticleRow> getRecentArticlesForCategories(
            Collection<Integer> categoryIds, 
            int numberOfRecentArticles) {
        if(categoryIds.isEmpty()) {
            return new ArrayList<ArticleRow>();
        }
        
        return template.query(
                "select A.Id, A.Title, A.Text, A.CreatedAt, A.UpdatedAt, A.ReadCount, A.CategoryId, A.UserId " +
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
            Collection<Integer> categoryIds, 
            int numberOfRecentArticles) {
        if(categoryIds.isEmpty()) {
            return new ArrayList<ArticleRow>();
        }
        
        return template.query(
                "select A.Id, A.Title, A.Text, A.CreatedAt, A.UpdatedAt, A.ReadCount, A.CategoryId, A.UserId " +
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
                "select limit :skip :take Id, Title, Text, CreatedAt, UpdatedAt, ReadCount, CategoryId, UserId " + 
                "from Articles where CategoryId = :categoryId", 
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
            articleRow.ReadCount = rs.getInt("ReadCount");
            articleRow.CategoryId = rs.getInt("CategoryId");
            articleRow.UserId = rs.getInt("UserId");
            return articleRow;
        }        
    }
    
    private static class ArticleRow2Mapper implements RowMapper<ArticleRow2> {
        @Override
        public ArticleRow2 mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleRow2 articleRow2 = new ArticleRow2();            
            
            articleRow2.ArticleId = rs.getInt("Id");
            articleRow2.Title = rs.getString("Title");
            articleRow2.Text = rs.getString("Text");
            articleRow2.CreatedAt = rs.getTimestamp("CreatedAt");
            articleRow2.UpdatedAt = rs.getTimestamp("UpdatedAt");
            articleRow2.ReadCount = rs.getInt("ReadCount");
            articleRow2.CommentCount = rs.getInt("CommentCount");
            articleRow2.VoteCount = rs.getInt("VoteCount");
            articleRow2.AverageVote = rs.getInt("AverageVote");
            
            BriefUser briefUser = new BriefUser();
            briefUser.UserId = rs.getInt("UserId");
            briefUser.Name = rs.getString("UserName");            
            articleRow2.User = briefUser;
            
            BriefCategory briefCategory = new BriefCategory();
            briefCategory.CategoryId = rs.getInt("CategoryId");
            briefCategory.Name = rs.getString("CategoryName");
            articleRow2.Category = briefCategory;
            
            return articleRow2;
        }        
    }
}