package me.loki2302;

import java.sql.Driver;

import javax.sql.DataSource;

import me.loki2302.faker.Faker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import com.googlecode.flyway.core.Flyway;

@Configuration
public class DataConfiguration {
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase embeddedDatabase = builder.setType(EmbeddedDatabaseType.HSQL).build();
        
        Flyway flyway = new Flyway();
        flyway.setDataSource(embeddedDatabase);
        flyway.migrate();
        
        return embeddedDatabase;
    }
    
    /*@Bean
    public DataSource dataSource() throws ClassNotFoundException {        
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>)Class.forName("org.hsqldb.jdbcDriver"));
        dataSource.setUrl("jdbc:hsqldb:file:mydb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
        
        return dataSource;
    }*/
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
        
    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
    
    @Bean
    public Faker faker() {
        return Faker.make();
    }
}