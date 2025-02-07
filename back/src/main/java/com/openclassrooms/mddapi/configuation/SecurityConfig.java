package com.openclassrooms.mddapi.configuation;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor to inject the custom JWT authentication filter.
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("✅ SecurityConfig LOADED !");
        http
                // Disable Cross-Site Request Forgery (CSRF) protection as this API uses stateless JWT authentication.
                .csrf(csrf -> csrf.disable())
                // Define authorization rules for endpoints.
                .authorizeHttpRequests(auth -> auth
                        .mvcMatchers("/auth/register", "/auth/login").permitAll() // ✅ Corrected requestMatchers
                        .mvcMatchers().authenticated()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        // Add the custom JWT authentication filter before the default UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // Return the SecurityFilterChain bean.
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Configure BCryptPasswordEncoder for encoding and validating passwords securely.
        return new BCryptPasswordEncoder();
    }
}
