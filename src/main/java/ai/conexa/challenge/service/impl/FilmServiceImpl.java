package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.FilmResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.service.FilmService;
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
public class FilmServiceImpl implements FilmService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public List<FilmResponse> getAll() {
        String url = String.format(endpoints.getFilmsAll());
        log.info("Fetching all films. URL: {}", url);
        return client.fetchObject(url, new TypeReference<MultipleResultResponse<FilmResponse>>() {})
                .getResult().stream().map(Result::getProperties).collect(Collectors.toList());
    }

    @Override
    public FilmResponse getById(Long id) {
        String url = String.format(endpoints.getFilmsById(), id);
        log.info("Fetching film by id. URL: {}", url);
        return client.fetchObject(url, new TypeReference<SingleResultResponse<FilmResponse>>() {})
                .getResult().getProperties();
    }

    @Override
    public List<FilmResponse> getByTitle(String title) {
        String url = String.format(endpoints.getFilmsByTitle(), title);
        log.info("Fetching film by title. URL: {}", url);
        return client.fetchObject(url, new TypeReference<MultipleResultResponse<FilmResponse>>() {})
                .getResult().stream().map(Result::getProperties).collect(Collectors.toList());
    }
}
