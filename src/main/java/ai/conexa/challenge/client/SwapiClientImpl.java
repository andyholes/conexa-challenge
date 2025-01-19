package ai.conexa.challenge.client;

import ai.conexa.challenge.exception.JsonParsingException;
import ai.conexa.challenge.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static ai.conexa.challenge.util.MessageConstants.SWAPI_FETCHING_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class SwapiClientImpl implements SwapiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> T fetchObject(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> T fetchObject(String url, TypeReference<T> typeReference) {
        try{
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return objectMapper.readValue(response.getBody(), typeReference);
        } catch (HttpClientErrorException e){
            if (e.getRawStatusCode() == 404){
                throw new ResourceNotFoundException();
            } else throw new ResourceAccessException(SWAPI_FETCHING_ERROR);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException();
        }
    }
}
