package com.example.maschinefactory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-ui/**").permitAll()
                        .requestMatchers("/api/customers/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.defaultSuccessUrl("/home")) // Customized form login
                .httpBasic(Customizer.withDefaults()); // HTTP Basic with default settings

        http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
