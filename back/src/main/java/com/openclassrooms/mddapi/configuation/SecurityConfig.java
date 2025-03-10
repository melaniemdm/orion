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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor to initialize the {@link SecurityConfig} class with a custom JWT authentication filter.
     * This constructor injects the {@link JwtAuthenticationFilter} which will be used to filter HTTP requests and validate JWT tokens.
     *
     * @param jwtAuthenticationFilter The custom filter that will intercept and validate JWT tokens in incoming HTTP requests.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the HTTP security for the application, including request authorization, session management, and custom JWT authentication.
     * <p>
     * This method disables CSRF protection as the application uses stateless JWT authentication. It also defines authorization rules for various
     * endpoints and ensures that only authenticated users can access protected resources. Additionally, it integrates a custom JWT authentication filter
     * into the Spring Security filter chain.
     *
     * @param http The {@link HttpSecurity} instance used to configure the security settings.
     * @return A {@link SecurityFilterChain} object containing the configured security rules.
     * @throws Exception If there is an error during the configuration process.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("✅ SecurityConfig LOADED !");
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable Cross-Site Request Forgery (CSRF) protection as this API uses stateless JWT authentication.
                .csrf(csrf -> csrf.disable())
                // Define authorization rules for endpoints.
                .authorizeHttpRequests(auth -> auth.mvcMatchers("/auth/register", "/auth/login").permitAll() // ✅ Corrected requestMatchers
                        .mvcMatchers().authenticated()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Add the custom JWT authentication filter before the default UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // Return the SecurityFilterChain bean.
        return http.build();
    }

    /**
     * Provides a {@link PasswordEncoder} bean for securely encoding and validating passwords.
     * <p>
     * This method configures the {@link BCryptPasswordEncoder} for use in the application. BCrypt is a strong hashing algorithm
     * that automatically handles salting and is commonly used for password hashing to protect sensitive information.
     * It is used to ensure that passwords are stored and validated securely in the database.
     *
     * @return A {@link PasswordEncoder} instance configured with BCrypt for secure password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Configure BCryptPasswordEncoder for encoding and validating passwords securely.
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Autorise Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées
        configuration.setAllowedHeaders(List.of("*")); // Tous les headers sont autorisés
        configuration.setAllowCredentials(true); // Permet l'envoi de cookies (JWT, sessions, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
