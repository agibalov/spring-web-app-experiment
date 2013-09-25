package me.loki2302.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.loki2302.dao.rows.SessionRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public SessionRow createSession(int userId, String token) {
        String rowUuid = UUID.randomUUID().toString();        
        template.update(
                "insert into Sessions(RowUuid, Token, UserId) values(:rowUuid, :token, :userId)",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid)
                    .addValue("token", token)
                    .addValue("userId", userId));
        
        SessionRow session = template.queryForObject(
                "select Id, Token, UserId from Sessions where RowUuid = :rowUuid",
                new MapSqlParameterSource()
                    .addValue("rowUuid", rowUuid),
                new SessionRowMapper());
                
        return session;
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