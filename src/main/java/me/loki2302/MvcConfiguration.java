package me.loki2302;

import me.loki2302.controllers.CurrentUserHandlerMethodArgumentResolver;
import me.loki2302.mvc.UserRelatedDetailsModelExtender;
import me.loki2302.rythm.FancyDateFormatTag;
import me.loki2302.rythm.FancyTimeFormatTag;
import me.loki2302.rythm.MarkdownTag;
import me.loki2302.rythm.spring.RythmViewResolver;
import org.rythmengine.RythmEngine;
import org.rythmengine.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.URISyntaxException;
import java.util.List;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private UserRelatedDetailsModelExtender userRelatedDetailsModelExtender; 
    
    @Autowired
    private CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver;
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserHandlerMethodArgumentResolver);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userRelatedDetailsModelExtender);
    }

    @Bean
    public ViewResolver rythmViewResolver() throws URISyntaxException {
        RythmEngine rythmEngine = new RythmEngine();
        rythmEngine.registerResourceLoader(new ClasspathResourceLoader(rythmEngine, "views"));
        rythmEngine.registerFastTag(new MarkdownTag());
        rythmEngine.registerFastTag(new FancyDateFormatTag());
        rythmEngine.registerFastTag(new FancyTimeFormatTag());
        rythmEngine.registerResourceLoader();

        RythmViewResolver rythmViewResolver = new RythmViewResolver(rythmEngine);
        rythmViewResolver.setPrefix("");
        rythmViewResolver.setSuffix(".html");

        return rythmViewResolver;
    }
}
