package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.exception.ResourceNotFoundException;
import ai.conexa.challenge.service.SwapiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ai.conexa.challenge.util.MessageConstants.RESOURCE_NOT_FOUND;
import static ai.conexa.challenge.util.MessageConstants.SWAPI_FETCHING_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class SwapiClientImpl implements SwapiClient {
    private final RestTemplate restTemplate;

    @Override
    public Map<String, Object> fetchPaginatedData(String url) {
        return fetchData(url);
    }

    @Override
    public List<Map<String, Object>> fetchResults(String url) {
        return getPropertiesinResultList(fetchData(url));
    }

    @Override
    public Map<String, Object> fetchSingleResult(String url) {
        try{
            return getPropertiesInResult(fetchData(url));
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
            } else {
                throw new RestClientException(SWAPI_FETCHING_ERROR);
            }
        }
    }

    private Map<String, Object> fetchData(String url) throws ResponseStatusException {
        ResponseEntity<String> response;
        response = restTemplate.getForEntity(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error parsing JSON response");
        }
    }

    private Map<String, Object> getProperties(Map<String, Object> result) {
        return (Map<String, Object>) result.get("properties");
    }

    private List<Map<String, Object>> getPropertiesinResultList(Map<String, Object> jsonString) {
        List<Map<String, Object>> results = (List<Map<String, Object>>) jsonString.get("result");
        return results.stream().map(this::getProperties).collect(Collectors.toList());
    }

    private Map<String, Object> getPropertiesInResult(Map<String, Object> jsonString) {
        Map<String, Object> result = (Map<String, Object>) jsonString.get("result");
        return getProperties(result);
    }
}
