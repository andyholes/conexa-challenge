package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.client.SwapiClient;
import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.PeopleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.service.PeopleService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleServiceImpl implements PeopleService {

    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getPeoplePaginated(), page, size);
        log.info("Fetching all people. URL: {}", url);
        return client.fetchObject(url, PaginatedResponse.class);
    }

    @Override
    public PeopleResponse getById(Long id) {
        String url = String.format(endpoints.getPeopleById(), id);
        log.info("Fetching person by id. URL: {}", url);
        return client.fetchObject(url, new TypeReference<SingleResultResponse<PeopleResponse>>() {})
                .getResult().getProperties();
    }

    @Override
    public List<PeopleResponse> getByName(String name) {
        String url = String.format(endpoints.getPeopleByName(), name);
        log.info("Fetching person by name. URL: {}", url);
        return client.fetchObject(url, new TypeReference<MultipleResultResponse<PeopleResponse>>() {})
                .getResult().stream().map(Result::getProperties).collect(Collectors.toList());
    }
}