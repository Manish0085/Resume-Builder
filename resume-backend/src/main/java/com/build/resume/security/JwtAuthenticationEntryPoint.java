package com.build.resume.security;

import com.build.resume.entity.User;
import com.build.resume.repository.UserRepository;
import com.build.resume.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationEntryPoint extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    public JwtAuthenticationEntryPoint(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String userId = null;
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userId = jwtUtil.getUserIdFromToken(token);
            }catch (Exception e){
                log.error("Token is not valid: {}", e.getMessage());
            }
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            try {
                if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token)){
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found."));
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e){
                log.error("Exception occurred while validating token");
            }
        }
        filterChain.doFilter(request, response);

    }
}
