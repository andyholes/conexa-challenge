package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.service.SwapiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SwapiClientImpl implements SwapiClient {
    private final RestTemplate restTemplate;

    @Override
    public <T> T fetchObject(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> T fetchObject(String url, TypeReference<T> typeReference) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data", e);
        }
    }
}
