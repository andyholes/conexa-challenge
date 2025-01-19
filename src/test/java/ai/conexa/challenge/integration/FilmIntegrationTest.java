package ai.conexa.challenge.integration;

import ai.conexa.challenge.config.SwapiApiConfig;
import ai.conexa.challenge.model.FilmResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
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
class FilmIntegrationTest {
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
    void testGetAllFilms_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetAllFilms_authorized_shouldReturn200() throws Exception {
        MultipleResultResponse<FilmResponse> response = new MultipleResultResponse<>(
                Collections.singletonList(new Result<>(
                        FilmResponse.builder()
                                .title("Test Film")
                                .director("Test Director")
                                .build()
                ))
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getFilmsAll()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/films")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Film")))
                .andExpect(jsonPath("$[0].director", is("Test Director")));
    }

    @Test
    void testGetFilmById_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/films/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetFilmById_authorized_shouldReturn200() throws Exception {
        SingleResultResponse<FilmResponse> response = new SingleResultResponse<>(
                new Result<>(
                        FilmResponse.builder()
                                .title("Test Film")
                                .director("Test Director")
                                .build()
                )
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getFilmsById()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/films/1")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Film")))
                .andExpect(jsonPath("$.director", is("Test Director")));
    }

    @Test
    void testGetFilmById_authorized_shouldReturn404() throws Exception {
        when(endpoints.getFilmsById()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/films/1")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(RESOURCE_NOT_FOUND)));
    }

    @Test
    void testGetFilmById_authorizedWithBadIdFormat_shouldReturn400() throws Exception {
        mockMvc.perform(get("/films/abc")
                .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetFilmByTitle_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/films/?title=Test Film"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetFilmByTitle_authorized_shouldReturn200() throws Exception {
        MultipleResultResponse<FilmResponse> response = new MultipleResultResponse<>(
                Collections.singletonList(new Result<>(
                        FilmResponse.builder()
                                .title("Test Film")
                                .director("Test Director")
                                .build()
                ))
        );

        stubFor(WireMock.get(urlPathMatching(URL)).willReturn(WireMock.aResponse()
                .withBody(objectMapper.writeValueAsString(response))));

        when(endpoints.getFilmsByTitle()).thenReturn(URL);
        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok(objectMapper.writeValueAsString(response)));

        mockMvc.perform(get("/films/?title=Test Film")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Film")))
                .andExpect(jsonPath("$[0].director", is("Test Director")));
    }

    @Test
    void testGetFilmByTitle_authorizedWithoutTitleParam_shouldReturn400() throws Exception {
        mockMvc.perform(get("/films/")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetFilmByTitle_authorizedWithBlankTitle_shouldReturn400() throws Exception {
        mockMvc.perform(get("/films/?title=")
                        .header(AUTHORIZATION, "Bearer " + jwtUtils.generateToken("admin")))
                .andExpect(status().isBadRequest());
    }
}