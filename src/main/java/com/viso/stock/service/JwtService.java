package com.viso.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.viso.stock.model.RoleEntity;
import com.viso.stock.model.TokenUserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String SECRET_KEY = "g2Yx4G48LQRcVsNUMKzOj4EXtKU9WjEPNDT48RAyCkM=";
    private final int TOKEN_VALIDITY = 1 * 60 * 60 * 1000;
    private final RoleService roleService;

    public JwtService(RoleService roleService) {
        this.roleService = roleService;
    }

    public String generateToken(UUID userId, String name, List<String> roles) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("name", name)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public UUID extractId(String token) {
        Claims claims = extractPayload(token);
        if (claims == null) {
            return null;
        }
        String subject = claims.getSubject();
        return UUID.fromString(subject);
    }

    public Claims extractPayload(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public TokenUserEntity extractTokenUser(String token) {
        Claims claims = extractPayload(token);
        if (claims == null) {
            return null;
        }
        UUID userId = UUID.fromString(claims.getSubject());
        String name = claims.get("name", String.class);
        @SuppressWarnings("unchecked")
        List<String> roleNames = (List<String>) claims.get("roles");
        System.out.println(roleNames.size() + " " + roleNames.get(0));
        Set<RoleEntity> roles = roleService.findByNames(roleNames.toArray(new String[0]));
        return new TokenUserEntity(userId, name, roles);
    }
}
