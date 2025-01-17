package ai.conexa.challenge.security;

import ai.conexa.challenge.exception.InvalidCredentialsException;
import ai.conexa.challenge.security.dto.LoginRequest;
import ai.conexa.challenge.security.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static ai.conexa.challenge.util.MessageConstants.INVALID_CREDENTIALS;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        if ("admin".equals(loginRequest.getUsername()) && "1234".equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(new LoginResponse("Bearer " + jwtUtils.generateToken(loginRequest.getUsername())));
        } else {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }
    }
}
