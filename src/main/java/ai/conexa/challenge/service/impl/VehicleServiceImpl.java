package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.mapper.BaseMapper;
import ai.conexa.challenge.model.mapper.VehicleMapper;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.VehicleResponse;
import ai.conexa.challenge.service.SwapiClient;
import ai.conexa.challenge.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getVehiclesPaginated(), page, size);
        return BaseMapper.mapToPaginatedResponse(client.fetchPaginatedData(url));
    }

    @Override
    public VehicleResponse getById(Long id) {
        String url = String.format(endpoints.getVehiclesById(), id);
        return VehicleMapper.mapToVehicleResponse(client.fetchSingleResult(url));
    }

    @Override
    public List<VehicleResponse> getByName(String name) {
        String url = String.format(endpoints.getVehiclesByName(), name);
        return client.fetchResults(url).stream()
                .map(VehicleMapper::mapToVehicleResponse).collect(Collectors.toList());
    }
}
