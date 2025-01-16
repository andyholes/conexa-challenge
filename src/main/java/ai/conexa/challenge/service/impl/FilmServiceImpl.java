package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.exception.ResourceNotFoundException;
import ai.conexa.challenge.model.mapper.FilmMapper;
import ai.conexa.challenge.model.response.FilmResponse;
import ai.conexa.challenge.service.FilmService;
import ai.conexa.challenge.util.SwapiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static ai.conexa.challenge.util.MessageConstants.FILM_NOT_FOUND;
import static ai.conexa.challenge.util.MessageConstants.SWAPI_FETCHING_ERROR;
import static ai.conexa.challenge.util.UrlConstants.TITLE_PARAM;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    public static final String RESOURCE = "/films";
    private final SwapiClient client;

    @Override
    public List<FilmResponse> getAll() {
        String url = RESOURCE;
        ResponseEntity<String> response = client.fetchData(url);
        return FilmMapper.mapToFilmResponseList(client.parseData(response));
    }

    @Override
    public FilmResponse getById(Long id) {
        String url = RESOURCE + "/" + id;
        ResponseEntity<String> response;
        try {
            response = client.fetchData(url);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                throw new ResourceNotFoundException(FILM_NOT_FOUND);
            } else {
                throw new RestClientException(SWAPI_FETCHING_ERROR);
            }
        }
        return FilmMapper.mapToFilmResponse(client.parseData(response));
    }

    @Override
    public List<FilmResponse> getByTitle(String title) {
        String url = RESOURCE + TITLE_PARAM + title;
        ResponseEntity<String> response = client.fetchData(url);
        return FilmMapper.mapToFilmResponseList(client.parseData(response));
    }

}
