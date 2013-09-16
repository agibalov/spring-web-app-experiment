package me.loki2302;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import me.loki2302.dao.CategoryDao.CategoryRow;
import me.loki2302.dao.UserDao.UserRow;
import me.loki2302.faker.Faker;
import me.loki2302.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@EnableWebMvc
@ComponentScan("me.loki2302")
public class AppConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt");
    }
    
    @Bean
    public SpringTemplateLoader templateLoader() {
        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setBasePath("/views/");
        templateLoader.setEncoding("UTF-8");
        templateLoader.setSuffix(".jade");
        return templateLoader;
    }

    @Bean
    public JadeConfiguration jadeConfiguration(SpringTemplateLoader templateLoader) {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setCaching(false);
        configuration.setSharedVariables(new HashMap<String, Object>() {{
            put("dateHelper", new DateHelper());
        }});
        configuration.setTemplateLoader(templateLoader);
        return configuration;
    }
    
    public static class DateHelper {
        public String makeDate(Date date) {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
        
        public String makeTime(Date date) {
            return new SimpleDateFormat("HH:mm:ss").format(date);
        }
    }

    @Bean
    public ViewResolver viewResolver(JadeConfiguration jadeConfiguration) {
        JadeViewResolver viewResolver = new JadeViewResolver();
        viewResolver.setConfiguration(jadeConfiguration);
        return viewResolver;
    }
    
    @Autowired
    private BlogService blogService;
    
    @PostConstruct
    public void PopulateDatabase() {
        Faker faker = Faker.make();
        
        final int numberOfUsers = 10;        
        List<UserRow> userRows = new ArrayList<UserRow>();
        for(int i = 0; i < numberOfUsers; ++i) {
            String userName = faker.Internet.userName();
            UserRow userRow = blogService.createUser(userName);
            userRows.add(userRow);
        }
        
        List<String> categoryNames = Arrays.asList("Porn", "Music", "Programming");
        List<CategoryRow> categoryRows = new ArrayList<CategoryRow>();
        for(String categoryName : categoryNames) {
            CategoryRow categoryRow = blogService.createCategory(categoryName);
            categoryRows.add(categoryRow);
        }        
        
        final int numberOfArticlesPerUserPerCategory = 13;
        for(UserRow userRow : userRows) {
            for(CategoryRow categoryRow : categoryRows) {
                for(int i = 0; i < numberOfArticlesPerUserPerCategory; ++i) {
                    String title = String.format(
                            "Article #%d in category %s by %s",
                            i,
                            categoryRow.Name,
                            userRow.Name);
                    
                    String text = String.format(
                            "Content for article #%d in category %s by %s",
                            i,
                            categoryRow.Name,
                            userRow.Name);
                    
                    blogService.createArticle(userRow.Id, categoryRow.Id, title, text);
                }
            }
        }
    }
}