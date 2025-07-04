package com.project.vestiart.services.interfaces;

import com.project.vestiart.models.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface IJwtService {

    Claims validateTokenAndGetClaims(String token);

    User retrieveUserByToken(String token);

    boolean isUserValid(String token);
}
