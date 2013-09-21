package me.loki2302;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .anonymous().disable()
            .securityContext().securityContextRepository(new MySecurityContextRepository(new HttpSessionSecurityContextRepository()));
                
        /*http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/account/sign-in")
                .usernameParameter("userName")
                .passwordParameter("password")
                .permitAll()
                .and()
            .logout()
                .permitAll();*/               
    }
    
    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("loki2302")
            .password("qwerty")
            .roles("USER");
    }
    
    public static class MySecurityContextRepository implements SecurityContextRepository {
        private final static Logger logger = LoggerFactory.getLogger(MySecurityContextRepository.class);

        private final HttpSessionSecurityContextRepository httpSessionSecurityContextRepository;
        
        public MySecurityContextRepository(HttpSessionSecurityContextRepository httpSessionSecurityContextRepository) {
            this.httpSessionSecurityContextRepository = httpSessionSecurityContextRepository;
        }
        
        @Override
        public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
            // read session token from cookies and return it (SecurityContex == Authentication)  
            // requestResponseHolder.getRequest().setAttribute("stuff", "qwerty"); // store things in here?
            logger.info("loadContext() {} {}", requestResponseHolder.getRequest(), requestResponseHolder.getResponse());
            return httpSessionSecurityContextRepository.loadContext(requestResponseHolder);
        }

        @Override
        public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
            // save SecurityContext (Authentication) to cookies
            logger.info("saveContext() {}, {}, {}", new Object[] {context, request, response});
            httpSessionSecurityContextRepository.saveContext(context, request, response);            
        }

        @Override
        public boolean containsContext(HttpServletRequest request) {
            // is there SecurityContext (Authentication) in cookies?
            logger.info("containsContext() {}", request);
            return httpSessionSecurityContextRepository.containsContext(request);
        }        
    }
}