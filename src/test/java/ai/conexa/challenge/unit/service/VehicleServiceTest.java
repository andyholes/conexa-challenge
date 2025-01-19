package ai.conexa.challenge.unit.service;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.VehicleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.service.SwapiClient;
import ai.conexa.challenge.service.impl.VehicleServiceImpl;
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
class VehicleServiceTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private SwapiClient client;

    @Mock
    private SwapiApiConfig endpoints;

    private static final String URL = "mocked_URL";

    @Test
    void testGetAllPaginated() {
        PaginatedResponse mockResponse = new PaginatedResponse();

        when(endpoints.getVehiclesPaginated()).thenReturn(URL);
        when(client.fetchObject(URL, PaginatedResponse.class)).thenReturn(mockResponse);

        PaginatedResponse response = vehicleService.getAllPaginated(1, 10);

        assertNotNull(response);
        verify(client, times(1)).fetchObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testGetAllPaginated_withEmptyResponse() {
        when(endpoints.getVehiclesPaginated()).thenReturn(URL);
        when(client.fetchObject(URL, PaginatedResponse.class)).thenReturn(null);

        PaginatedResponse response = vehicleService.getAllPaginated(1, 10);

        assertNull(response);
        verify(client, times(1)).fetchObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testGetById() {
        SingleResultResponse<VehicleResponse> mockResponse = new SingleResultResponse<>(new Result<>(new VehicleResponse()));

        when(endpoints.getVehiclesById()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(mockResponse);

        VehicleResponse response = vehicleService.getById(1L);

        assertNotNull(response);
        assertEquals(mockResponse.getResult().getProperties(), response);
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByName() {
        List<Result<VehicleResponse>> mockResults = Collections.singletonList(new Result<>(new VehicleResponse()));

        when(endpoints.getVehiclesByName()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(mockResults));

        List<VehicleResponse> response = vehicleService.getByName("Speeder");

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    void testGetByName_withNoResults() {
        when(endpoints.getVehiclesByName()).thenReturn(URL);
        when(client.fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new MultipleResultResponse<>(Collections.emptyList()));

        List<VehicleResponse> response = vehicleService.getByName("NonExistentVehicle");

        assertTrue(response.isEmpty());
        verify(client, times(1)).fetchObject(eq(URL), ArgumentMatchers.any(TypeReference.class));
    }
}
