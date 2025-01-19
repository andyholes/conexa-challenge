package ai.conexa.challenge.security;

import ai.conexa.challenge.exception.InvalidCredentialsException;
import ai.conexa.challenge.exception.handler.ErrorResponse;
import ai.conexa.challenge.security.dto.LoginRequest;
import ai.conexa.challenge.security.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authorization Controller", description = "Let's you login to the application and get a JWT token to access the rest of the API.")
public class AuthController {
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Login to the application", description = "Since this is a demo application, the only valid credentials are username: admin, password: 1234. If you provide any other credentials, you will get an invalid credentials exception.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        if ("admin".equals(loginRequest.getUsername()) && "1234".equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(new LoginResponse("Bearer " + jwtUtils.generateToken(loginRequest.getUsername())));
        } else {
            throw new InvalidCredentialsException();
        }
    }
}
