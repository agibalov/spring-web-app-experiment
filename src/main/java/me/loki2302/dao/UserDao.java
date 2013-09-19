package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import me.loki2302.dao.rows.UserRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
       
    public UserRow getUser(int userId) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Name, Password from Users where Id = :userId",
                new MapSqlParameterSource()
                    .addValue("userId", userId),
                new UserRowMapper()));
    }
    
    public List<UserRow> getUsers(Iterable<Integer> userIds) {
        return template.query(
                "select Id, Name, Password from Users where Id in (:userIds)",
                new MapSqlParameterSource()
                    .addValue("userIds", userIds),
                new UserRowMapper());
    }
        
    public UserRow findUserByUserName(String userName) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Name, Password from Users where Name = :userName",
                new MapSqlParameterSource()
                    .addValue("userName", userName),
                new UserRowMapper()));
    }
    
    public UserRow createUser(String userName, String password) {
        final String rowUuid = UUID.randomUUID().toString();                
        template.update(
                "insert into Users(RowUuid, Name, Password) values(:rowUuid, :userName, :password)",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid)
                    .addValue("userName", userName)
                    .addValue("password", password));
        
        UserRow user = template.queryForObject(
                "select Id, Name, Password from Users where RowUuid = :rowUuid",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid),
                new UserRowMapper());
                
        return user;
    }
    
    private static class UserRowMapper implements RowMapper<UserRow> {
        @Override
        public UserRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRow userRow = new UserRow();
            userRow.Id = rs.getInt("Id");
            userRow.Name = rs.getString("Name");
            userRow.Password = rs.getString("Password");
            return userRow;
        }
    }
}