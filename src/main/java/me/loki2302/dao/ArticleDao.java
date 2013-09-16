package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
            final String text) {
        
        final String rowUuid = UUID.randomUUID().toString();                
        template.update(
                "insert into Articles(RowUuid, Title, Text, CategoryId, UserId) " + 
                "values(:rowUuid, :title, :text, :categoryId, :userId)",
                new HashMap<String, Object>() {{
                    put("rowUuid", rowUuid);
                    put("title", title);
                    put("text", text);
                    put("categoryId", categoryId);
                    put("userId", userId);
                }});
        
        ArticleRow articleRow = template.queryForObject(
                "select Id, Title, Text, CategoryId, UserId from Articles where RowUuid = :rowUuid",
                new HashMap<String, Object>() {{
                    put("rowUuid", rowUuid);
                }},
                new ArticleRowMapper());
                
        return articleRow;
    }
    
    public static class ArticleRow {
        public int Id;
        public String Title;
        public String Text;
        public int CategoryId;
        public int UserId;
    }
    
    private static class ArticleRowMapper implements RowMapper<ArticleRow> {
        @Override
        public ArticleRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleRow articleRow = new ArticleRow();
            articleRow.Id = rs.getInt("Id");
            articleRow.Title = rs.getString("Title");
            articleRow.Text = rs.getString("Text");
            articleRow.CategoryId = rs.getInt("CategoryId");
            articleRow.UserId = rs.getInt("UserId");
            return articleRow;
        }        
    }
}