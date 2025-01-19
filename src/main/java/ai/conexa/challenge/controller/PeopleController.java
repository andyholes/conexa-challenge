package ai.conexa.challenge.controller;

import ai.conexa.challenge.exception.handler.ErrorResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.PeopleResponse;
import ai.conexa.challenge.service.PeopleService;
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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
@Validated
@Tag(name = "People Controller", description = "Includes endpoints to get people by ID, search people by name, and get a paginated list of people.")
public class PeopleController {
    private final PeopleService peopleService;

    @GetMapping
    @Operation(summary = "Get a paginated list of people", description = "Retrieves a paginated list of people from the database. Pagination is achieved using the 'page' and 'size' parameters, where 'page' is the page number and 'size' is the number of items per page. The default values are 'page=1' and 'size=10'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of people"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid pagination parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PaginatedResponse> getAllPaginated(@RequestParam(defaultValue = "1") @Min(1) int page,
                                                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ResponseEntity.ok(peopleService.getAllPaginated(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Retrieves a specific person by their ID. If the person with the provided ID does not exist, a 404 Not Found error will be returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the person"),
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PeopleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(peopleService.getById(id));
    }

    @GetMapping("/")
    @Operation(summary = "Search people by name", description = "Search for people in the database by their name. This allows filtering of people based on a provided name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved people by name"),
            @ApiResponse(responseCode = "400", description = "Bad request, name parameter is required", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<PeopleResponse>> getByName(@RequestParam @NotBlank String name) {
        return ResponseEntity.ok(peopleService.getByName(name));
    }
}
