package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import me.loki2302.dao.rows.CategoryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public int createCategory(String categoryName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();        
        template.update(
                "insert into Categories(Name) values(:name)",
                new MapSqlParameterSource()
                    .addValue("name", categoryName),
                keyHolder);
        
        return (Integer)keyHolder.getKey();
    }
    
    public CategoryRow getCategory(int categoryId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Name from Categories where Id = :categoryId",
                new MapSqlParameterSource()
                    .addValue("categoryId", categoryId),
                new CategoryRowMapper()));
    }
    
    public List<CategoryRow> getCategories(Iterable<Integer> categoryIds) {
        return template.query(
                "select Id, Name from Categories where Id in (:categoryIds)",
                new MapSqlParameterSource()
                    .addValue("categoryIds", categoryIds),
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