package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.StarshipResponse;
import ai.conexa.challenge.service.StarshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ai.conexa.challenge.util.UrlConstants.BASE_MAPPING;

@RestController
@RequestMapping(BASE_MAPPING + "/starships")
@RequiredArgsConstructor
public class StarshipController {
    private final StarshipService starshipService;

    @GetMapping
    public ResponseEntity<PaginatedResponse> getAllPaginated(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(starshipService.getAllPaginated(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StarshipResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(starshipService.getById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<StarshipResponse>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(starshipService.getByName(name));
    }
}