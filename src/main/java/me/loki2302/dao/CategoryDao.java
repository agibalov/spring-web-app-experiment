package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.dao.rows.UserRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public CategoryRow createCategory(final String categoryName) {
        final String rowUuid = UUID.randomUUID().toString();                
        template.update(
                "insert into Categories(RowUuid, Name) values(:rowUuid, :name)",
                new HashMap<String, Object>() {{
                    put("rowUuid", rowUuid);
                    put("name", categoryName);
                }});
        
        CategoryRow categoryRow = template.queryForObject(
                "select Id, Name from Categories where RowUuid = :rowUuid",
                new HashMap<String, Object>() {{
                    put("rowUuid", rowUuid);
                }},
                new CategoryRowMapper());
        
        return categoryRow;
    }
    
    public CategoryRow getCategory(final int categoryId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Name from Categories where Id = :categoryId",
                new HashMap<String, Object>() {{
                    put("categoryId", categoryId);
                }},
                new CategoryRowMapper()));
    }
    
    public List<CategoryRow> getCategories(final Iterable<Integer> categoryIds) {
        return template.query(
                "select Id, Name from Categories where Id in (:categoryIds)",
                new HashMap<String, Object>() {{
                    put("categoryIds", categoryIds);
                }},
                new CategoryRowMapper());
    }
    
    public List<CategoryRow> getCategories() {
        return template.query(
                "select Id, Name from Categories", 
                new CategoryRowMapper());
    }
    
    private static class CategoryRowMapper implements RowMapper<CategoryRow> {
        @Override
        public CategoryRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            CategoryRow categoryRow = new CategoryRow();
            categoryRow.Id = rs.getInt("Id");
            categoryRow.Name = rs.getString("Name");
            return categoryRow;
        }
    }
}