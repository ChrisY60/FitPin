package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.User;

public interface JwtService {

    String generateToken(User user);

    String extractUsername(String token);

    boolean isTokenValid(String token);

}