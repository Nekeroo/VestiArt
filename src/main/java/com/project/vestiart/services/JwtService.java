package com.project.vestiart.services;

import com.project.vestiart.models.User;
import com.project.vestiart.services.interfaces.IJwtService;
import com.project.vestiart.services.interfaces.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final IUserService IUserService;

    private Key key;

    public JwtService(IUserService IUserService) {
        this.IUserService = IUserService;
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
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        Claims claims = validateTokenAndGetClaims(token);

        String username = claims.getSubject();

        return IUserService.getUser(username);
    }

    public boolean isUserValid(String token) {

        String tokenFiltered = token.replace("Bearer ", "");

        User user = retrieveUserByToken(tokenFiltered);

        return user != null;
    }

}
