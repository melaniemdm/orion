package com.openclassrooms.mddapi.configuation;

import com.openclassrooms.mddapi.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //used to manage and validate JWT tokens
    private JwtService jwtService;

    // Constructor injecting the JwtService service to interact with JWT tokens
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println(" JwtAuthenticationFilter is running for request: " + request.getRequestURI());
        // Extraction and validation of the JWT token
        extractToken(request)// Get the token from the Authorization header
                .flatMap(this::getValidUsername)// Validates the token and extracts the username if valid
                .filter(username -> SecurityContextHolder.getContext().getAuthentication() == null) // Checks that there is no active authentication in the context
                .ifPresent(username ->{
                    System.out.println("Authenticated user: " + username);
                    setAuthentication(username, request);
                });

        filterChain.doFilter(request, response);
    }

    // Method to extract the token from the Authorization header
    private Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return Optional.of(token);
        }
        return Optional.empty();
    }

    // Method to validate the token and get the username if it is valid
    private Optional<String> getValidUsername(String token) {
        String idUser = jwtService.getUsernameFromToken(token);
        System.out.println(" Extracted idUser: " + idUser);
        System.out.println(" Extracted token: " + token);
        if (idUser != null && jwtService.validateToken(token, idUser)) {
            System.out.println("Token is valid for user: " + idUser);
            return Optional.of(idUser);
        }
        System.out.println(" Invalid Token");
        return Optional.empty();
    }

    // Method to authenticate user in Spring security context
    private void setAuthentication(String username, HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        username, null, null
                )
        );
    }


}