package ai.conexa.challenge.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static ai.conexa.challenge.util.UrlConstants.SWAPI_BASE_URL;

@Component
public class SwapiClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> fetchData(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "PostmanRuntime/7.43.0");
        HttpEntity requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(SWAPI_BASE_URL + url, HttpMethod.GET, requestEntity, String.class);
    }

    public Map<String, Object> parseData(ResponseEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
