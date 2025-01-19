package ai.conexa.challenge.unit.service;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.FilmResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.service.SwapiClient;
import ai.conexa.challenge.service.impl.FilmServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @InjectMocks
    private FilmServiceImpl filmService;

    @Mock
    private SwapiClient client;

    @Mock
    private SwapiApiConfig endpoints;

    private static final String URL = "mocked_URL";

    @Test
    void testGetAll() {
        List<Result<FilmResponse>> mockResults = Collections.singletonList(new Result<>(new FilmResponse()));

        when(endpoints.getFilmsAll()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(mockResults));

        List<FilmResponse> response = filmService.getAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetAll_withEmptyResponse() {
        when(endpoints.getFilmsAll()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(Collections.emptyList()));

        List<FilmResponse> response = filmService.getAll();

        assertTrue(response.isEmpty());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetById() {
        SingleResultResponse<FilmResponse> mockResponse = new SingleResultResponse<>(new Result<>(new FilmResponse()));

        when(endpoints.getFilmsById()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(mockResponse);

        FilmResponse response = filmService.getById(1L);

        assertNotNull(response);
        assertEquals(mockResponse.getResult().getProperties(), response);
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByTitle() {
        List<Result<FilmResponse>> mockResults = Collections.singletonList(new Result<>(new FilmResponse()));

        when(endpoints.getFilmsByTitle()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(mockResults));

        List<FilmResponse> response = filmService.getByTitle("A New Hope");

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByTitle_withNoResults() {
        when(endpoints.getFilmsByTitle()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(Collections.emptyList()));

        List<FilmResponse> response = filmService.getByTitle("NonExistentFilm");

        assertTrue(response.isEmpty());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }
}
