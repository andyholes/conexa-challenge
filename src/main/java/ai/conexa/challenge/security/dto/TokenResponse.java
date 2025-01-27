package ai.conexa.challenge.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenResponse {
    @Schema(example = "Bearer eyJhbG...")
    private String token;

    public TokenResponse(String token) {
        this.token = "Bearer " + token;
    }
}
