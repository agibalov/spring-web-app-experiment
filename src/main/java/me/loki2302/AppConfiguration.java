package me.loki2302;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.loki2302.controllers.CurrentUserHandlerMethodArgumentResolver;
import me.loki2302.jadehelpers.JadeDateHelper;
import me.loki2302.jadehelpers.JadeMarkdownHelper;
import me.loki2302.jadehelpers.SpringSecurityHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@EnableWebMvc
@ComponentScan("me.loki2302")
public class AppConfiguration extends WebMvcConfigurerAdapter {
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
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public void postHandle(
                    HttpServletRequest request, 
                    HttpServletResponse response, 
                    Object handler, 
                    ModelAndView modelAndView) throws Exception {
                if(modelAndView == null) {
                    return;
                }
                
                ModelMap modelMap = modelAndView.getModelMap();
                if(modelMap == null) {
                    return;
                }
                
                modelMap.addAttribute("magik", "MAGIK HERE!!!111one");
            }            
        });
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

        Map<String, Object> sharedVariables = new HashMap<String, Object>();
        sharedVariables.put("dateHelper", new JadeDateHelper());
        sharedVariables.put("markdownHelper", new JadeMarkdownHelper());
        sharedVariables.put("security", new SpringSecurityHelper());
        configuration.setSharedVariables(sharedVariables);

        configuration.setTemplateLoader(templateLoader);
        return configuration;
    }

    @Bean
    public ViewResolver viewResolver(JadeConfiguration jadeConfiguration) {
        JadeViewResolver viewResolver = new JadeViewResolver();
        viewResolver.setConfiguration(jadeConfiguration);
        return viewResolver;
    }        
}