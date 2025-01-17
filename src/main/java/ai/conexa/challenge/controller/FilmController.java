package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.FilmResponse;
import ai.conexa.challenge.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class FilmController {
    private final FilmService filmServiceService;

    @GetMapping
    public ResponseEntity<List<FilmResponse>> getAll() {
        return ResponseEntity.ok(filmServiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(filmServiceService.getById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<FilmResponse>> getByTitle(@RequestParam @NotBlank String title) {
        return ResponseEntity.ok(filmServiceService.getByTitle(title));
    }
}
