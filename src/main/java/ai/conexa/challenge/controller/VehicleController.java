package ai.conexa.challenge.controller;

import ai.conexa.challenge.exception.handler.ErrorResponse;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.VehicleResponse;
import ai.conexa.challenge.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Controller", description = "Includes endpoints to get vehicles by ID, search vehicles by name, and get a paginated list of vehicles.")
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Get a paginated list of vehicles", description = "Retrieves a paginated list of vehicles from the database. Pagination is achieved using the 'page' and 'size' parameters, where 'page' is the page number and 'size' is the number of items per page. The default values are 'page=1' and 'size=10'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of vehicles"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid pagination parameters", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PaginatedResponse> getAllPaginated(@RequestParam(defaultValue = "1") @Min(1) int page,
                                                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ResponseEntity.ok(vehicleService.getAllPaginated(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieves a specific vehicle by its ID. If the vehicle with the provided ID does not exist, a 404 Not Found error will be returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the vehicle"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<VehicleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search vehicles by name", description = "Search for vehicles in the database by their name. This allows filtering of vehicles based on a provided name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles by name"),
            @ApiResponse(responseCode = "400", description = "Bad request, name parameter is required", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<VehicleResponse>> getByName(@RequestParam @NotBlank String name) {
        return ResponseEntity.ok(vehicleService.getByName(name));
    }
}
