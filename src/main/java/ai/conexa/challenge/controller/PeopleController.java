package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.PeopleResponse;
import ai.conexa.challenge.service.PeopleService;
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
public class PeopleController {
    private final PeopleService peopleService;

    @GetMapping
    public ResponseEntity<PaginatedResponse> getAllPaginated(@RequestParam(defaultValue = "1") @Min(1) int page,
                                                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ResponseEntity.ok(peopleService.getAllPaginated(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeopleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(peopleService.getById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PeopleResponse>> getByName(@RequestParam @NotBlank String name) {
        return ResponseEntity.ok(peopleService.getByName(name));
    }
}
