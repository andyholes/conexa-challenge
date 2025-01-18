package ai.conexa.challenge.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    @Schema(example = "admin")
    private String username;

    @NotBlank
    @Schema(example = "1234")
    private String password;
}
