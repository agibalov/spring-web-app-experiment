package me.loki2302;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.loki2302.controllers.CurrentUserHandlerMethodArgumentResolver;
import me.loki2302.mvc.UserRelatedDetailsModelExtender;
import me.loki2302.rythm.FancyDateFormatTag;
import me.loki2302.rythm.FancyTimeFormatTag;
import me.loki2302.rythm.MarkdownTag;

import org.rythmengine.template.ITemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ctlok.springframework.web.servlet.view.rythm.RythmConfigurator;
import com.ctlok.springframework.web.servlet.view.rythm.RythmViewResolver;

@EnableWebMvc
@ComponentScan("me.loki2302")
public class MvcConfiguration extends WebMvcConfigurerAdapter {    
    @Autowired
    private UserRelatedDetailsModelExtender userRelatedDetailsModelExtender; 
    
    @Autowired
    private CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt");
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserHandlerMethodArgumentResolver);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userRelatedDetailsModelExtender);
    }
    
    @Bean
    public RythmConfigurator rythmConfigurator() {
        RythmConfigurator rythmConfigurator = new RythmConfigurator();
        rythmConfigurator.setMode("dev");        
        rythmConfigurator.setTempDirectory("./");
        rythmConfigurator.setRootDirectory("/views/");
        rythmConfigurator.setImplicitPackages(Arrays.asList(
                "me.loki2302.mvc.*",
                "me.loki2302.controllers.*",
                "me.loki2302.service.dto.*",
                "me.loki2302.service.dto.article.*",
                "me.loki2302.service.dto.category.*",
                "me.loki2302.service.dto.user.*"));
        
        List<ITemplate> tags = new ArrayList<ITemplate>();
        tags.add(new MarkdownTag());
        tags.add(new FancyDateFormatTag());
        tags.add(new FancyTimeFormatTag());
        rythmConfigurator.setTags(tags);
        
        return rythmConfigurator;
    }
    
    @Bean
    public RythmViewResolver rythmViewResolver(RythmConfigurator rythmConfigurator) {
        RythmViewResolver rythmViewResolver = new RythmViewResolver(rythmConfigurator);
        rythmViewResolver.setPrefix("/views/");
        rythmViewResolver.setSuffix(".html");
        return rythmViewResolver;
    }        
}