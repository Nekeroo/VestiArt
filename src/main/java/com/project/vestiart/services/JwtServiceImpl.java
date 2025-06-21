package com.project.vestiart.services;

import com.project.vestiart.models.User;
import com.project.vestiart.services.interfaces.JwtService;
import com.project.vestiart.services.interfaces.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserService userService;

    private Key key;

    public JwtServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public Claims validateTokenAndGetClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public User retrieveUserByToken(String token) {
        Claims claims = validateTokenAndGetClaims(token);

        String username = claims.getSubject();

        return userService.getUser(username);
    }

}
