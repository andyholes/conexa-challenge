package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testMapToPaginatedResponse() {
        Map<String, Object> jsonResults = new HashMap<>();
        jsonResults.put("uid", "1");
        jsonResults.put("name", "Luke Skywalker");
        jsonResults.put("url", "https://www.swapi.tech/api/people/1");

        Map<String, Object> jsonString = new HashMap<>();
        jsonString.put("total_records", 82);
        jsonString.put("total_pages", 82);
        jsonString.put("results", Arrays.asList(jsonResults));


        PaginatedResponse response = BaseMapper.mapToPaginatedResponse(jsonString);

        assertEquals(82, response.getTotalRecords());
        assertEquals(82, response.getTotalPages());

        List<Result> results = response.getResults();
        assertEquals(1, results.size());
        Result result = results.get(0);
        assertEquals("1", result.getUid());
        assertEquals("Luke Skywalker", result.getName());
        assertEquals("https://www.swapi.tech/api/people/1", result.getUrl());
    }
}