package ai.conexa.challenge.security;

import ai.conexa.challenge.security.dto.TokenResponse;
import ai.conexa.challenge.security.dto.UserRequest;

public interface AuthService {
    TokenResponse register(UserRequest userRequest);
    TokenResponse login(UserRequest userRequest);
    void delete(Long id);
}
