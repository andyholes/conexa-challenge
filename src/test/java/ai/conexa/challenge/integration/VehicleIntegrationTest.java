package ai.conexa.challenge.integration;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.VehicleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.PaginatedResult;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.model.generic.SingleResultResponse;
import ai.conexa.challenge.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static ai.conexa.challenge.util.MessageConstants.RESOURCE_NOT_FOUND;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WireMockTest(httpPort = 8090)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class VehicleIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private SwapiApiConfig endpoints;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtils jwtUtils;
    private static final String URL = "mock-url";

    @Test
    void testGetVehiclePaginated_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/vehicles?page=1&size=2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetVehiclePaginated_authorized_shouldReturn200() throws Exception {
        PaginatedResponse response = new PaginatedResponse(1, 1, Collections.singletonList(
                new PaginatedResult("1", "Falcon", "https://www.swapi.tech/api/vehicles/1")));

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getVehiclesPaginated()).thenReturn(URL);
        when(restTemplate.getForObject(URL, PaginatedResponse.class))
                .thenReturn(response);

        mockMvc.perform(get("/vehicles?page=1&size=2")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].uid", is("1")))
                .andExpect(jsonPath("$.results[0].name", is("Falcon")))
                .andExpect(jsonPath("$.results[0].url", is("https://www.swapi.tech/api/vehicles/1")));
    }

    @Test
    void testGetVehiclePaginated_authorizedWithPageEqualsToZero_shouldReturn400() throws Exception {
        mockMvc.perform(get("/vehicles?page=0&size=2")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetVehiclePaginated_authorizedWithSizeEqualsToZero_shouldReturn400() throws Exception {
        mockMvc.perform(get("/vehicles?page=1&size=0")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetVehicleById_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetVehicleById_authorized_shouldReturn200() throws Exception {
        SingleResultResponse<VehicleResponse> response = new SingleResultResponse<>(
                new Result<>(
                        VehicleResponse.builder()
                                .name("Falcon")
                                .build()
                )
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getVehiclesById()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/vehicles/1")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Falcon")));
    }

    @Test
    void testGetVehicleById_authorized_shouldReturn404() throws Exception {
        when(endpoints.getVehiclesById()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/vehicles/1")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(RESOURCE_NOT_FOUND)));
    }

    @Test
    void testGetVehicleById_authorizedWithBadIdFormat_shouldReturn400() throws Exception {
        mockMvc.perform(get("/vehicles/abc")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetVehicleByName_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/vehicles/?name=Falcon"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetVehicleByName_authorized_shouldReturn200() throws Exception {
        MultipleResultResponse<VehicleResponse> response = new MultipleResultResponse<>(
                Collections.singletonList(new Result<>(
                        VehicleResponse.builder()
                                .name("Falcon")
                                .build()
                ))
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getVehiclesByName()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/vehicles/?name=Falcon")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Falcon")));
    }

    @Test
    void testGetVehicleByName_authorizedWithoutNameParam_shouldReturn400() throws Exception {
        mockMvc.perform(get("/vehicles/")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetVehicleByName_authorizedWithBlankName_shouldReturn400() throws Exception {
        mockMvc.perform(get("/vehicles/?name=")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }
}