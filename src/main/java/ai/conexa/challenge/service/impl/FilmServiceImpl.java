package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.mapper.FilmMapper;
import ai.conexa.challenge.model.response.FilmResponse;
import ai.conexa.challenge.service.FilmService;
import ai.conexa.challenge.service.SwapiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final SwapiApiConfig endpoints;
    private final SwapiClient client;

    @Override
    public List<FilmResponse> getAll() {
        return client.fetchResults(endpoints.getFilmsAll()).stream()
                .map(FilmMapper::mapToFilmResponse).collect(Collectors.toList());
    }

    @Override
    public FilmResponse getById(Long id) {
        String url = String.format(endpoints.getFilmsById(), id);
        return FilmMapper.mapToFilmResponse(client.fetchSingleResult(url));
    }

    @Override
    public List<FilmResponse> getByTitle(String title) {
        String url = String.format(endpoints.getFilmsByTitle(), title);
        return client.fetchResults(url).stream()
                .map(FilmMapper::mapToFilmResponse).collect(Collectors.toList());
    }
}
