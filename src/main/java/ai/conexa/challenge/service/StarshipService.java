package ai.conexa.challenge.service;

import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.StarshipResponse;

import java.util.List;

public interface StarshipService {
    PaginatedResponse getAllPaginated(int page, int size);

    StarshipResponse getById(Long id);

    List<StarshipResponse> getByName(String name);
}
