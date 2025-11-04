package com.viso.stock.config;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.viso.stock.model.TokenUserEntity;
import com.viso.stock.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        
        try {
            TokenUserEntity tokenUser = jwtService.extractTokenUser(token);
            System.out.println("Extracted token user: " + (tokenUser != null ? tokenUser.getName() + tokenUser.getRoles().size() : "null"));
            if (tokenUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Set<GrantedAuthority> authorities = tokenUser.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(permission -> {
                            System.out.println("Mapping permission: " + permission.getName().getAuthority());
                            return new SimpleGrantedAuthority(permission.getName().getAuthority());
                        })
                        .collect(Collectors.toSet());
                        

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        tokenUser, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            System.err.println("JWT Authentication failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}