package com.example.platstyle.config;

import com.example.platstyle.config.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;
/*
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        //auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }*/

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/forgotpassword", "/signup", "/register", "/authentication").permitAll()
                .antMatchers("/admin/**", "/uploads/*").hasAnyRole("ADMIN")
                .antMatchers("/stylist/**").hasAnyRole("ADMIN","STYLIST")
                .antMatchers("/user/**").hasAnyRole("ADMIN","STYLIST" ,"USER")
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin(   ).loginPage("/login")
                .loginProcessingUrl("/authentication")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/user/shop").permitAll()
                .and().logout().permitAll()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

}
