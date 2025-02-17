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

    /**
     * Constructs a JwtAuthenticationFilter with the given JwtService.
     * This constructor is used to inject the JwtService dependency for interacting with JWT tokens.
     * The JwtService will handle operations such as extracting user information from the token and validating it.
     *
     * @param jwtService The service responsible for handling JWT token extraction and validation.
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * This method is part of the filter chain that intercepts HTTP requests and processes JWT authentication.
     * It extracts the JWT from the request, validates it, and if valid, authenticates the user in the security context.
     * <p>
     * The method performs the following steps:
     * 1. Extracts the JWT token from the Authorization header of the request.
     * 2. Validates the JWT token and extracts the username associated with it if the token is valid.
     * 3. If the username is valid and there is no active authentication in the security context,
     * it sets the authentication for the user in the security context.
     * 4. After processing, the method proceeds to the next filter in the chain by calling {@link FilterChain#doFilter}.
     *
     * @param request     The HTTP request being processed. It contains the headers and other information needed to extract the JWT token.
     * @param response    The HTTP response that will be sent back to the client after the filter chain is executed.
     * @param filterChain The chain of filters that the request and response will pass through.
     *                    This allows the request to continue processing after the authentication logic.
     * @throws ServletException If the request or response cannot be handled properly.
     * @throws IOException      If an input or output error occurs during the processing of the request or response.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(" JwtAuthenticationFilter is running for request: " + request.getRequestURI());
        // Extraction and validation of the JWT token
        extractToken(request)// Get the token from the Authorization header
                .flatMap(this::getValidUsername)// Validates the token and extracts the username if valid
                .filter(username -> SecurityContextHolder.getContext().getAuthentication() == null) // Checks that there is no active authentication in the context
                .ifPresent(username -> {
                    System.out.println("Authenticated user: " + username);
                    setAuthentication(username, request);
                });

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the "Authorization" header of the HTTP request.
     * <p>
     * This method looks for the "Authorization" header in the request and checks if it starts with "Bearer ".
     * If a valid token is found, it extracts the token part and returns it as an {@link Optional}.
     * If the token is missing or not in the expected format, it returns an empty {@link Optional}.
     *
     * @param request The HTTP request containing the "Authorization" header.
     * @return An {@link Optional} containing the JWT token if found, otherwise an empty {@link Optional}.
     */
    private Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return Optional.of(token);
        }
        return Optional.empty();
    }

    /**
     * Extracts the username from the JWT token and validates the token.
     * <p>
     * This method attempts to extract the username (or user ID) from the provided JWT token and validates the token's integrity.
     * If the token is valid, it returns the username wrapped in an {@link Optional}.
     * If the token is invalid, it returns an empty {@link Optional}.
     *
     * @param token The JWT token to extract and validate.
     * @return An {@link Optional} containing the username if the token is valid, otherwise an empty {@link Optional}.
     */
    private Optional<String> getValidUsername(String token) {
        String idUser = jwtService.getUsernameFromToken(token);

        if (idUser != null && jwtService.validateToken(token, idUser)) {
            System.out.println("Token is valid for user: " + idUser);
            return Optional.of(idUser);
        }
        System.out.println(" Invalid Token");
        return Optional.empty();
    }

    /**
     * Sets the authentication in the security context using the provided username.
     * <p>
     * This method takes the extracted username and sets it as the authenticated user in the
     * Spring Security context. It creates a {@link UsernamePasswordAuthenticationToken} with
     * the provided username, but no password or authorities (roles), as this method is
     * typically used for stateless authentication with JWT tokens.
     *
     * @param username The username of the authenticated user.
     * @param request  The HTTP request used to obtain the token (included for context but not used directly).
     */
    private void setAuthentication(String username, HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, null));
    }


}