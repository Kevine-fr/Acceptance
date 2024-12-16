package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Désactive la protection CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello", "/auth/register", "/auth/login" , "auth/user", "auth/user/{id}").permitAll()  // Permet l'accès à /auth/register, /hello et /auth/login sans authentification
                        .anyRequest().authenticated()  // Requiert une authentification pour toutes les autres routes
                )
                .formLogin(withDefaults())  // Active le formulaire de login
                .httpBasic(withDefaults()); // Active l'authentification HTTP de base (si nécessaire)
        return http.build();
    }
}
