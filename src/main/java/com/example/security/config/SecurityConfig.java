package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ExceptionTranslationWebFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public MapReactiveUserDetailsService userDetailsRepository() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN", "USER")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public ExceptionTranslationWebFilter exceptionTranslationWebFilter() {
        ExceptionTranslationWebFilter filter = new ExceptionTranslationWebFilter();
        filter.setServerAccessDeniedHandler((e, d) -> {
            e.getResponse().getHeaders().add("Location", "/error/accessDenied");
            e.getResponse().setStatusCode(HttpStatus.FOUND);
            return Mono.empty();
        });
        return filter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange()
                .pathMatchers("/resources/**").permitAll()
                .pathMatchers("/login").permitAll()
                .anyExchange().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .and()
            .logout()
                .and()
            .csrf()
                .and()
            .exceptionHandling();
        return http.build();
    }

}
