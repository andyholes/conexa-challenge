package ai.conexa.challenge.unit.service;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.StarshipResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.client.SwapiClient;
import ai.conexa.challenge.service.impl.StarshipServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StarshipServiceTest {

    @InjectMocks
    private StarshipServiceImpl starshipService;

    @Mock
    private SwapiClient client;

    @Mock
    private SwapiApiConfig endpoints;
    private static final String URL = "mocked_URL";

    @Test
    void testGetAllPaginated() {
        PaginatedResponse mockResponse = new PaginatedResponse();

        when(endpoints.getStarshipsPaginated()).thenReturn(URL);
        when(client.fetchObject(URL, PaginatedResponse.class)).thenReturn(mockResponse);

        PaginatedResponse response = starshipService.getAllPaginated(1, 10);

        assertNotNull(response);
        verify(client, times(1)).fetchObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testGetAllPaginated_withEmptyResponse() {
        when(endpoints.getStarshipsPaginated()).thenReturn(URL);
        when(client.fetchObject(URL, PaginatedResponse.class)).thenReturn(null);

        PaginatedResponse response = starshipService.getAllPaginated(1, 10);

        assertNull(response);
        verify(client, times(1)).fetchObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testGetById() {
        SingleResultResponse<StarshipResponse> mockResponse = new SingleResultResponse<>(new Result<>(new StarshipResponse()));

        when(endpoints.getStarshipsById()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(mockResponse);

        StarshipResponse response = starshipService.getById(1L);

        assertNotNull(response);
        assertEquals(mockResponse.getResult().getProperties(), response);
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByName() {
        List<Result<StarshipResponse>> mockResults = Collections.singletonList(new Result<>(new StarshipResponse()));

        when(endpoints.getStarshipsByName()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(mockResults));

        List<StarshipResponse> response = starshipService.getByName("X-Wing");

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByName_withNoResults() {
        when(endpoints.getStarshipsByName()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(Collections.emptyList()));

        List<StarshipResponse> response = starshipService.getByName("NonExistentStarship");

        assertTrue(response.isEmpty());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }
}
