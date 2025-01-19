package ai.conexa.challenge.unit.service;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.PeopleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.client.SwapiClient;
import ai.conexa.challenge.service.impl.PeopleServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {

    @InjectMocks
    private PeopleServiceImpl peopleService;

    @Mock
    private SwapiClient client;

    @Mock
    private SwapiApiConfig endpoints;

    private static final String URL = "mocked_URL";

    @Test
    void testGetAllPaginated() {
        PaginatedResponse mockResponse = new PaginatedResponse();

        when(endpoints.getPeoplePaginated()).thenReturn(URL);
        when(client.fetchObject(URL, PaginatedResponse.class)).thenReturn(mockResponse);

        PaginatedResponse response = peopleService.getAllPaginated(1, 10);

        assertNotNull(response);
        verify(client, times(1)).fetchObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testGetAllPaginated_withEmptyResponse() {
        when(endpoints.getPeoplePaginated()).thenReturn(URL);
        when(client.fetchObject(URL, PaginatedResponse.class)).thenReturn(null);

        PaginatedResponse response = peopleService.getAllPaginated(1, 10);

        assertNull(response);
        verify(client, times(1)).fetchObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testGetById() {
        SingleResultResponse<PeopleResponse> mockResponse = new SingleResultResponse<>(new Result<>(new PeopleResponse()));

        when(endpoints.getPeopleById()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(mockResponse);

        PeopleResponse response = peopleService.getById(1L);

        assertNotNull(response);
        assertEquals(mockResponse.getResult().getProperties(), response);
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByName() {
        List<Result<PeopleResponse>> mockResults = Collections.singletonList(new Result<>(new PeopleResponse()));

        when(endpoints.getPeopleByName()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(mockResults));

        List<PeopleResponse> response = peopleService.getByName("Luke");

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByName_withNoResults() {
        when(endpoints.getPeopleByName()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(Collections.emptyList()));

        List<PeopleResponse> response = peopleService.getByName("NonExistentPerson");

        assertTrue(response.isEmpty());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }
}
