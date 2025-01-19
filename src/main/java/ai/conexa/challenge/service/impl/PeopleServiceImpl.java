package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.PeopleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.service.PeopleService;
import ai.conexa.challenge.service.SwapiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private static final Logger log = LoggerFactory.getLogger(PeopleServiceImpl.class);
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getPeoplePaginated(), page, size);
        log.info("Fetching paginated people data from URL: {}", url);
        PaginatedResponse response = client.fetchObject(url, PaginatedResponse.class);
        log.info("Fetched {} people data for page {}: {}", response.getResults().size(), page, response);
        return response;
    }

    @Override
    public PeopleResponse getById(Long id) {
        String url = String.format(endpoints.getPeopleById(), id);
        log.info("Fetching person data by ID from URL: {}", url);
        SingleResultResponse<PeopleResponse> response = client.fetchObject(url, new TypeReference<SingleResultResponse<PeopleResponse>>() {
        });
        log.info("Fetched person data by ID: {}", response.getResult());
        return response.getResult().getProperties();
    }

    @Override
    public List<PeopleResponse> getByName(String name) {
        String url = String.format(endpoints.getPeopleByName(), name);
        log.info("Fetching people data by name from URL: {}", url);
        MultipleResultResponse<PeopleResponse> response = client.fetchObject(url, new TypeReference<MultipleResultResponse<PeopleResponse>>() {
        });
        List<PeopleResponse> people = response.getResult().stream()
                .map(Result::getProperties)
                .collect(Collectors.toList());
        log.info("Fetched {} people data by name: {}", people.size(), name);
        return people;
    }
}