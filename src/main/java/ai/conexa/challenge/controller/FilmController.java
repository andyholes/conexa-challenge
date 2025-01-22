package ai.conexa.challenge.controller;

import ai.conexa.challenge.exception.handler.ErrorResponse;
import ai.conexa.challenge.model.FilmResponse;
import ai.conexa.challenge.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
@Tag(name = "Film Controller", description = "Includes endpoints to get films by ID, search films by title, and get a list of all films")
public class FilmController {
    private final FilmService filmServiceService;

    @GetMapping
    @Operation(summary = "Get all available films", description = "Retrieves a list of all films available in the database. This endpoint does not support pagination, as films are returned in their entirety.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of films"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<FilmResponse>> getAll() {
        return ResponseEntity.ok(filmServiceService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get film by ID", description = "Retrieves a specific film by its ID. Provides detailed information about the selected film from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the film"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Film not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FilmResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(filmServiceService.getById(id));
    }

    @GetMapping("/")
    @Operation(summary = "Search films by title", description = "Search for films in the database by their title. This allows filtering of films based on a provided title.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved films by title"),
            @ApiResponse(responseCode = "400", description = "Bad request, title parameter is required", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<FilmResponse>> getByTitle(@RequestParam @NotBlank String title) {
        return ResponseEntity.ok(filmServiceService.getByTitle(title));
    }
}
