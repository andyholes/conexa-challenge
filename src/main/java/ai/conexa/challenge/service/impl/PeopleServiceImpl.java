package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.exception.ResourceNotFoundException;
import ai.conexa.challenge.model.mapper.BaseMapper;
import ai.conexa.challenge.model.mapper.PeopleMapper;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.PeopleResponse;
import ai.conexa.challenge.service.PeopleService;
import ai.conexa.challenge.util.SwapiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static ai.conexa.challenge.util.MessageConstants.PERSON_NOT_FOUND;
import static ai.conexa.challenge.util.MessageConstants.SWAPI_FETCHING_ERROR;
import static ai.conexa.challenge.util.UrlConstants.NAME_PARAM;
import static ai.conexa.challenge.util.UrlConstants.PAGE_PARAM;
import static ai.conexa.challenge.util.UrlConstants.SIZE_PARAM;

@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {
    public static final String RESOURCE = "/people";
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = RESOURCE + PAGE_PARAM + page + SIZE_PARAM + size;
        ResponseEntity<String> response = client.fetchData(url);
        return BaseMapper.mapToPaginatedResponse(client.parseData(response));
    }

    @Override
    public PeopleResponse getById(Long id) {
        String url = RESOURCE + "/" + id;
        ResponseEntity<String> response;
        try {
            response = client.fetchData(url);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                throw new ResourceNotFoundException(PERSON_NOT_FOUND);
            } else {
                throw new RestClientException(SWAPI_FETCHING_ERROR);
            }
        }
        return PeopleMapper.mapToPeopleResponse(client.parseData(response));
    }

    @Override
    public List<PeopleResponse> getByName(String name) {
        String url = RESOURCE + NAME_PARAM + name;
        ResponseEntity<String> response = client.fetchData(url);
        return PeopleMapper.mapToPeopleResponseList(client.parseData(response));
    }
}
