package ai.conexa.challenge.service;

import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.VehicleResponse;

import java.util.List;

public interface VehicleService {
    PaginatedResponse getAllPaginated(int page, int size);

    VehicleResponse getById(Long id);

    List<VehicleResponse> getByName(String name);
}
