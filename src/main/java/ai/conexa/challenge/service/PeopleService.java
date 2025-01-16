package ai.conexa.challenge.service;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.PeopleResponse;

import java.util.List;

public interface PeopleService {

    PaginatedResponse getAllPaginated(int page, int size);

    PeopleResponse getById(Long id);

    List<PeopleResponse> getByName(String name);
}
