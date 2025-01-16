package ai.conexa.challenge.service;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.VehicleResponse;

import java.util.List;

public interface VehicleService {
    PaginatedResponse getAllPaginated(int page, int size);

    VehicleResponse getById(Long id);

    List<VehicleResponse> getByName(String name);
}
