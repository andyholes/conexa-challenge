package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.VehicleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.client.SwapiClient;
import ai.conexa.challenge.service.VehicleService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getVehiclesPaginated(), page, size);
        log.info("Fetching all vehicles. URL: {}", url);
        return client.fetchObject(url, PaginatedResponse.class);
    }

    @Override
    public VehicleResponse getById(Long id) {
        String url = String.format(endpoints.getVehiclesById(), id);
        log.info("Fetching vehicle by id. URL: {}", url);
        return client.fetchObject(url, new TypeReference<SingleResultResponse<VehicleResponse>>() {})
                .getResult().getProperties();
    }

    @Override
    public List<VehicleResponse> getByName(String name) {
        String url = String.format(endpoints.getVehiclesByName(), name);
        log.info("Fetching vehicle by name. URL: {}", url);
        return client.fetchObject(url, new TypeReference<MultipleResultResponse<VehicleResponse>>() {})
                .getResult().stream().map(Result::getProperties).collect(Collectors.toList());
    }
}
