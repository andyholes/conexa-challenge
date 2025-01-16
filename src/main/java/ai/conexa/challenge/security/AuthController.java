package ai.conexa.challenge.security;

import ai.conexa.challenge.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static ai.conexa.challenge.util.MessageConstants.INVALID_CREDENTIALS;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        if ("admin".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            return ResponseEntity.ok("Bearer " + jwtUtils.generateToken(loginRequest.getUsername()));
        } else {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }
    }
}
