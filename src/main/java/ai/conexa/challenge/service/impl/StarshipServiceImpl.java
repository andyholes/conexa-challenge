package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.StarshipResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.service.StarshipService;
import ai.conexa.challenge.client.SwapiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StarshipServiceImpl implements StarshipService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getStarshipsPaginated(), page, size);
        log.info("Fetching all starships. URL: {}", url);
        return client.fetchObject(url, PaginatedResponse.class);
    }

    @Override
    public StarshipResponse getById(Long id) {
        String url = String.format(endpoints.getStarshipsById(), id);
        log.info("Fetching starship by id. URL: {}", url);
        return client.fetchObject(url, new TypeReference<SingleResultResponse<StarshipResponse>>() {})
                .getResult().getProperties();
    }

    @Override
    public List<StarshipResponse> getByName(String name) {
        String url = String.format(endpoints.getStarshipsByName(), name);
        log.info("Fetching starship by name. URL: {}", url);
        return client.fetchObject(url, new TypeReference<MultipleResultResponse<StarshipResponse>>() {})
                .getResult().stream().map(Result::getProperties).collect(Collectors.toList());
    }
}