package com.springcloud.server_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/book-service/books/messages").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/book-service/books/messages/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/book-service/books").permitAll()
                .antMatchers(HttpMethod.GET, "/book-service/books/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/book-service/books").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/book-service/books").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/book-service/books/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/book-service/books/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/rating-service/ratings/messages").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/rating-service/ratings/messages/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/rating-service/ratings").permitAll()
                .antMatchers(HttpMethod.GET, "/rating-service/ratings/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/rating-service/ratings").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/rating-service/ratings").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/rating-service/ratings/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/rating-service/ratings/*").hasRole("ADMIN")
                .antMatchers("/eureka/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/discovery/*").hasRole("ADMIN")
                .anyRequest().authenticated().and()
                .formLogin().usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/success").and()
                .logout().permitAll().and()
                .csrf().disable();
    }
}
