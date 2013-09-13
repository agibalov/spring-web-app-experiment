package me.loki2302;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SomethingDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public int getSomething() {
        return template.queryForObject(
                "select * from Something", 
                new HashMap<String, Object>(), 
                Integer.class);
    }        
}
