package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.PeopleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleMapperTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testMapToPeopleResponse() {
        Map<String, Object> peopleProperties = new HashMap<>();
        peopleProperties.put("height", "172");
        peopleProperties.put("mass", "77");
        peopleProperties.put("hair_color", "blond");
        peopleProperties.put("skin_color", "fair");
        peopleProperties.put("eye_color", "blue");
        peopleProperties.put("birth_year", "19BBY");
        peopleProperties.put("gender", "male");
        peopleProperties.put("created", "2025-01-18T06:24:39.980Z");
        peopleProperties.put("edited", "2025-01-18T06:24:39.980Z");
        peopleProperties.put("name", "Luke Skywalker");
        peopleProperties.put("homeworld", "https://www.swapi.tech/api/planets/1");
        peopleProperties.put("url", "https://www.swapi.tech/api/people/1");
        peopleProperties.put("films", Collections.singletonList("https://www.swapi.tech/api/films/1"));
        peopleProperties.put("species", Collections.singletonList("https://www.swapi.tech/api/species/1"));
        peopleProperties.put("starships", Collections.singletonList("https://www.swapi.tech/api/starships/2"));
        peopleProperties.put("vehicles", Collections.singletonList("https://www.swapi.tech/api/vehicles/4"));

        PeopleResponse response = PeopleMapper.mapToPeopleResponse(peopleProperties);

        assertEquals("Luke Skywalker", response.getName());
        assertEquals("19BBY", response.getBirthYear());
        assertEquals("blue", response.getEyeColor());
        assertEquals("male", response.getGender());
        assertEquals("blond", response.getHairColor());
        assertEquals("172", response.getHeight());
        assertEquals("77", response.getMass());
        assertEquals("fair", response.getSkinColor());
        assertEquals("https://www.swapi.tech/api/planets/1", response.getHomeworld());
        assertEquals("https://www.swapi.tech/api/people/1", response.getUrl());
        assertEquals("2025-01-18T06:24:39.980Z", response.getCreated());
        assertEquals("2025-01-18T06:24:39.980Z", response.getEdited());
        assertEquals("172", peopleProperties.get("height"));
        assertEquals("77", peopleProperties.get("mass"));
        assertEquals("blond", peopleProperties.get("hair_color"));
        assertEquals("fair", peopleProperties.get("skin_color"));
        assertEquals("blue", peopleProperties.get("eye_color"));
        assertEquals("19BBY", peopleProperties.get("birth_year"));
        assertEquals("male", peopleProperties.get("gender"));
        assertEquals("2025-01-18T06:24:39.980Z", peopleProperties.get("created"));
        assertEquals("2025-01-18T06:24:39.980Z", peopleProperties.get("edited"));
        assertEquals("Luke Skywalker", peopleProperties.get("name"));
        assertEquals("https://www.swapi.tech/api/planets/1", peopleProperties.get("homeworld"));
        assertEquals("https://www.swapi.tech/api/people/1", peopleProperties.get("url"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/films/1"), peopleProperties.get("films"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/species/1"), peopleProperties.get("species"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/starships/2"), peopleProperties.get("starships"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/vehicles/4"), peopleProperties.get("vehicles"));
        assertEquals(1, response.getFilms().size());
        assertEquals("https://www.swapi.tech/api/films/1", response.getFilms().get(0));
        assertEquals(1, response.getSpecies().size());
        assertEquals("https://www.swapi.tech/api/species/1", response.getSpecies().get(0));
        assertEquals(1, response.getStarships().size());
        assertEquals("https://www.swapi.tech/api/starships/2", response.getStarships().get(0));
        assertEquals(1, response.getVehicles().size());
        assertEquals("https://www.swapi.tech/api/vehicles/4", response.getVehicles().get(0));
    }
}