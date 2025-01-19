package ai.conexa.challenge.unit.service;

import ai.conexa.challenge.exception.ResourceNotFoundException;
import ai.conexa.challenge.model.PeopleResponse;
import ai.conexa.challenge.model.generic.MultipleResultResponse;
import ai.conexa.challenge.model.generic.PaginatedResponse;
import ai.conexa.challenge.model.generic.Result;
import ai.conexa.challenge.service.impl.SwapiClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SwapiClientTest {

    @InjectMocks
    private SwapiClientImpl swapiClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private static final String URL = "mocked_URL";

    @Test
    void testFetchObject_withClassType() {
        PaginatedResponse mockedResponse = new PaginatedResponse();

        when(restTemplate.getForObject(anyString(), eq(PaginatedResponse.class)))
                .thenReturn(mockedResponse);

        PaginatedResponse response = swapiClient.fetchObject(URL, PaginatedResponse.class);

        assertNotNull(response);
        assertEquals(mockedResponse, response);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(PaginatedResponse.class));
    }

    @Test
    void testFetchObject_withTypeReference() throws Exception {
        String jsonResponse = "{\n" +
                "  \"message\": \"ok\",\n" +
                "  \"result\": [\n" +
                "    {\n" +
                "      \"properties\": {\n" +
                "        \"height\": \"228\",\n" +
                "        \"mass\": \"112\",\n" +
                "        \"hair_color\": \"brown\",\n" +
                "        \"skin_color\": \"unknown\",\n" +
                "        \"eye_color\": \"blue\",\n" +
                "        \"birth_year\": \"200BBY\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"created\": \"2025-01-18T06:24:39.980Z\",\n" +
                "        \"edited\": \"2025-01-18T06:24:39.980Z\",\n" +
                "        \"name\": \"Chewbacca\",\n" +
                "        \"homeworld\": \"https://www.swapi.tech/api/planets/14\",\n" +
                "        \"url\": \"https://www.swapi.tech/api/people/13\"\n" +
                "      },\n" +
                "      \"description\": \"A person within the Star Wars universe\",\n" +
                "      \"_id\": \"5f63a36eee9fd7000499be4e\",\n" +
                "      \"uid\": \"13\",\n" +
                "      \"__v\": 0\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        MultipleResultResponse<PeopleResponse> mockedResponse = new MultipleResultResponse<>(Collections.singletonList(new Result<>(
                PeopleResponse.builder()
                .name("Chewbacca")
                .height("228")
                .mass("112")
                .hairColor("brown")
                .skinColor("unknown")
                .eyeColor("blue")
                .birthYear("200BBY")
                .gender("male")
                .created("2025-01-18T06:24:39.980Z")
                .edited("2025-01-18T06:24:39.980Z")
                .homeworld("https://www.swapi.tech/api/planets/14")
                .url("https://www.swapi.tech/api/people/13")
                .build()
        )));

        when(restTemplate.getForEntity(URL, String.class)).thenReturn(ResponseEntity.ok(jsonResponse));
        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(mockedResponse);
        MultipleResultResponse<PeopleResponse> response = swapiClient.fetchObject(URL, new TypeReference<MultipleResultResponse<PeopleResponse>>() {});

        verify(restTemplate, times(1)).getForEntity(URL, String.class);
        verify(objectMapper, times(1)).readValue(eq(jsonResponse), any(TypeReference.class));

        assertEquals(mockedResponse, response);
    }

    @Test
    void testFetchObject_shouldThrowInternalError_whenJsonProcessingExceptionOccurs() throws JsonProcessingException {
        TypeReference<MultipleResultResponse<PeopleResponse>> typeReference = new TypeReference<MultipleResultResponse<PeopleResponse>>() {};

        when(restTemplate.getForEntity(URL, String.class))
                .thenReturn(ResponseEntity.ok("{\"message\": \"ok\", \"result\": []}"));

        when(objectMapper.readValue(anyString(), eq(typeReference)))
                .thenThrow(JsonProcessingException.class);

        assertThrows(InternalError.class, () -> {
            swapiClient.fetchObject(URL, typeReference);
        });

        verify(restTemplate, times(1)).getForEntity(URL, String.class);
        verify(objectMapper, times(1)).readValue(anyString(), eq(typeReference));
    }

    @Test
    void testFetchObject_shouldThrowResourceAccessException() throws JsonProcessingException {
        TypeReference<MultipleResultResponse<PeopleResponse>> typeReference = new TypeReference<MultipleResultResponse<PeopleResponse>>() {};

        when(restTemplate.getForEntity(URL, String.class)).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceAccessException.class, () -> {
            swapiClient.fetchObject(URL, typeReference);
        });

        verify(restTemplate, times(1)).getForEntity(URL, String.class);
        verify(objectMapper, times(0)).readValue(anyString(), eq(typeReference));
    }
}