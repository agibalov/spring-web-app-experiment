package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import me.loki2302.dao.rows.SessionRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public int createSession(int userId, String token) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        template.update(
                "insert into Sessions(Token, UserId) values(:token, :userId)",
                new MapSqlParameterSource()
                    .addValue("token", token)
                    .addValue("userId", userId),
                keyHolder);
        
        return (Integer)keyHolder.getKey();
    }
    
    public SessionRow getSession(int id) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Token, UserId from Sessions where id = :id",
                new MapSqlParameterSource()
                    .addValue("id", id),
                new SessionRowMapper()));
    } 
    
    public SessionRow getSession(String token) {
        return DataAccessUtils.singleResult(template.query(
                "select Id, Token, UserId from Sessions where Token = :token",
                new MapSqlParameterSource()
                    .addValue("token", token),
                new SessionRowMapper()));
    }
    
    private static class SessionRowMapper implements RowMapper<SessionRow> {
        @Override
        public SessionRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            SessionRow sessionRow = new SessionRow();
            sessionRow.Id = rs.getInt("Id");
            sessionRow.Token = rs.getString("Token");
            sessionRow.UserId = rs.getInt("UserId");
            return sessionRow;
        }
    }
}