package ai.conexa.challenge.exception.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(example = "An error occurred while processing the request")
    private String message;
}