package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.service.SwapiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static ai.conexa.challenge.util.MessageConstants.JSON_PARSE_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class SwapiClientImpl implements SwapiClient {
    private final RestTemplate restTemplate;

    @Override
    public <T> T fetchObject(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> T fetchObject(String url, TypeReference<T> typeReference) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.getBody(), typeReference);
        } catch (JsonProcessingException e) {
            throw new InternalError(JSON_PARSE_ERROR);
        }
    }
}
