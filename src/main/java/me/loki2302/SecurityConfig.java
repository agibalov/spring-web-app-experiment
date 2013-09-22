package me.loki2302;

import me.loki2302.auth.CookieSecurityContextRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public SecurityConfig() {
        super(true);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/robots.txt");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //.csrf().disable()
            //.anonymous().disable()
            //.securityContext().securityContextRepository(new VoyeurSecurityContextRepositoryProxy(new HttpSessionSecurityContextRepository()));
            .securityContext().securityContextRepository(new CookieSecurityContextRepository())
            ;
        
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
}