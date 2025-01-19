package ai.conexa.challenge.integration;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.StarshipResponse;
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
class StarshipIntegrationTest {
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
    void testGetStarshipPaginated_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/starships?page=1&size=2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetStarshipPaginated_authorized_shouldReturn200() throws Exception {
        PaginatedResponse response = new PaginatedResponse(1, 1, Collections.singletonList(
                new PaginatedResult("1", "CR90 corvette", "https://www.swapi.tech/api/starships/1")));

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getStarshipsPaginated()).thenReturn(URL);
        when(restTemplate.getForObject(URL, PaginatedResponse.class)).thenReturn(response);

        mockMvc.perform(get("/starships?page=1&size=1")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].uid", is("1")))
                .andExpect(jsonPath("$.results[0].name", is("CR90 corvette")))
                .andExpect(jsonPath("$.results[0].url", is("https://www.swapi.tech/api/starships/1")));
    }

    @Test
    void testGetStarshipPaginated_authorizedWithPageEqualsToZero_shouldReturn400() throws Exception {
        mockMvc.perform(get("/starships?page=0&size=1")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStarshipPaginated_authorizedWithSizeEqualsToZero_shouldReturn400() throws Exception {
        mockMvc.perform(get("/starships?page=1&size=0")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStarshipById_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/starships/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetStarshipById_authorized_shouldReturn200() throws Exception {
        SingleResultResponse<StarshipResponse> response = new SingleResultResponse<>(
                new Result<>(
                        StarshipResponse.builder()
                                .name("CR90 corvette")
                                .build()
                )
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getStarshipsById()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/starships/1")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("CR90 corvette")));
    }

    @Test
    void testGetStarshipById_authorized_shouldReturn404() throws Exception {
        when(endpoints.getStarshipsById()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/starships/1")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(RESOURCE_NOT_FOUND)));
    }

    @Test
    void testGetStarshipById_authorizedWithBadIdFormat_shouldReturn400() throws Exception {
        mockMvc.perform(get("/starships/abc")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStarshipByName_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/starships/?name=CR90 corvette"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetStarshipByName_authorized_shouldReturn200() throws Exception {
        MultipleResultResponse<StarshipResponse> response = new MultipleResultResponse<>(
                Collections.singletonList(new Result<>(
                        StarshipResponse.builder()
                                .name("CR90 corvette")
                                .build()
                ))
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getStarshipsByName()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/starships/?name=CR90 corvette")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("CR90 corvette")));
    }

    @Test
    void testGetStarshipByName_authorizedWithoutNameParam_shouldReturn400() throws Exception {
        mockMvc.perform(get("/starships/")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStarshipByName_authorizedWithBlankName_shouldReturn400() throws Exception {
        mockMvc.perform(get("/starships/?name=")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }
}