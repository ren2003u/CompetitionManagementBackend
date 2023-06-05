package com.example.demo.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Question 1: What is SecurityContextHolder.getContext().getAuthentication() == null used for?
            // Answer: SecurityContextHolder is a component of Spring Security that stores details about the current security context, including details about the current user.
            // SecurityContextHolder.getContext().getAuthentication() returns the Authentication object stored in the security context.
            // This Authentication object represents the currently authenticated user, if any.
            // If getAuthentication() returns null, it means that no authentication information is stored in the security context, or in other words, the user is not authenticated yet.
            // This check is done to ensure that Spring Security does not perform unnecessary authentication operations if the user is already authenticated.


            // Question 2: What does this line of code do?
            // Answer: The UserDetailsService is another component of Spring Security.
            // It is used to retrieve the user's details from the database.
            // The loadUserByUsername method loads a user record based on the username and returns a fully populated UserDetails object, which includes the username, password, and a set of authorities (roles).
            // The UserDetails object and the JWT token are used together to validate the token because the token was originally created for a specific user (the username is often included as a subject in the token).
            // The validation checks that the token was issued for the user that's currently trying to access the API, and that the token is not expired.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                // Question 3: Why do we create UsernamePasswordAuthenticationToken?
                // Answer: UsernamePasswordAuthenticationToken is a specific implementation of the Authentication interface, which Spring Security uses to represent the token for a user that's authenticated with a username and password.
                // When you create a new instance of UsernamePasswordAuthenticationToken and put it in the SecurityContext, you're telling Spring Security that the current user is authenticated.
                // This object not only includes the user's username and password but also includes the user's roles (authorities), which Spring Security uses for access control checks.
                // In your code, you're passing null as the credentials because the password is not needed after the user is authenticated, and the user's roles are loaded from the UserDetails object.
                // After setting this token in the SecurityContext, subsequent code (including your API handlers) can trust that the user is authenticated.
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}