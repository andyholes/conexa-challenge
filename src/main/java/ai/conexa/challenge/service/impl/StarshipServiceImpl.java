package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.mapper.BaseMapper;
import ai.conexa.challenge.model.mapper.StarshipMapper;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.StarshipResponse;
import ai.conexa.challenge.service.StarshipService;
import ai.conexa.challenge.service.SwapiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StarshipServiceImpl implements StarshipService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getStarshipsPaginated(), page, size);
        return BaseMapper.mapToPaginatedResponse(client.fetchPaginatedData(url));
    }

    @Override
    public StarshipResponse getById(Long id) {
        String url = String.format(endpoints.getStarshipsById(), id);
        return StarshipMapper.mapToStarshipResponse(client.fetchSingleResult(url));
    }

    @Override
    public List<StarshipResponse> getByName(String name) {
        String url = String.format(endpoints.getStarshipsByName(), name);
        return client.fetchResults(url).stream()
                .map(StarshipMapper::mapToStarshipResponse).collect(Collectors.toList());
    }
}