package ai.conexa.challenge.service;

import ai.conexa.challenge.model.FilmResponse;

import java.util.List;

public interface FilmService {
    List<FilmResponse> getAll();
    FilmResponse getById(Long id);
    List<FilmResponse> getByTitle(String name);
}
