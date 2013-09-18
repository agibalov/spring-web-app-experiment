package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import me.loki2302.dao.rows.CategoryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public CategoryRow createCategory(String categoryName) {
        final String rowUuid = UUID.randomUUID().toString();                
        template.update(
                "insert into Categories(RowUuid, Name) values(:rowUuid, :name)",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid)
                    .addValue("name", categoryName));
        
        CategoryRow categoryRow = template.queryForObject(
                "select Id, Name from Categories where RowUuid = :rowUuid",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid),
                new CategoryRowMapper());
        
        return categoryRow;
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