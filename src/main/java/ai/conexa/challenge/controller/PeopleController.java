package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.PeopleResponse;
import ai.conexa.challenge.service.PeopleService;
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
@RequestMapping(BASE_MAPPING + "/people")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService peopleService;

    @GetMapping
    public ResponseEntity<PaginatedResponse> getAllPaginated(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(peopleService.getAllPaginated(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeopleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(peopleService.getById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PeopleResponse>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(peopleService.getByName(name));
    }
}
