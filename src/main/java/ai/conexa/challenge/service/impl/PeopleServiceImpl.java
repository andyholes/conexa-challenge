package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.mapper.BaseMapper;
import ai.conexa.challenge.model.mapper.PeopleMapper;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.PeopleResponse;
import ai.conexa.challenge.service.PeopleService;
import ai.conexa.challenge.service.SwapiClient;
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
        return BaseMapper.mapToPaginatedResponse(client.fetchPaginatedData(url));
    }

    @Override
    public PeopleResponse getById(Long id) {
        String url = String.format(endpoints.getPeopleById(), id);
        return PeopleMapper.mapToPeopleResponse(client.fetchSingleResult(url));
    }

    @Override
    public List<PeopleResponse> getByName(String name) {
        String url = String.format(endpoints.getPeopleByName(), name);
        return client.fetchResults(url).stream()
                .map(PeopleMapper::mapToPeopleResponse).collect(Collectors.toList());
    }
}
