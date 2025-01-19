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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = String.format(endpoints.getPeoplePaginated(), page, size);
        return client.fetchObject(url, PaginatedResponse.class);
    }

    @Override
    public PeopleResponse getById(Long id) {
        String url = String.format(endpoints.getPeopleById(), id);
        return client.fetchObject(url, new TypeReference<SingleResultResponse<PeopleResponse>>(){})
                .getResult().getProperties();
    }

    @Override
    public List<PeopleResponse> getByName(String name) {
        String url = String.format(endpoints.getPeopleByName(), name);
        return client.fetchObject(url, new TypeReference<MultipleResultResponse<PeopleResponse>>() {})
                .getResult().stream().map(Result::getProperties).collect(Collectors.toList());
    }
}
