package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.FilmResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testMapToFilmResponse() {
        Map<String, Object> filmProperties = new HashMap<>();
        filmProperties.put("characters", Collections.singletonList("https://www.swapi.tech/api/people/1"));
        filmProperties.put("planets", Collections.singletonList("https://www.swapi.tech/api/planets/1"));
        filmProperties.put("starships", Collections.singletonList("https://www.swapi.tech/api/starships/2"));
        filmProperties.put("vehicles", Collections.singletonList("https://www.swapi.tech/api/vehicles/4"));
        filmProperties.put("species", Collections.singletonList("https://www.swapi.tech/api/species/1"));
        filmProperties.put("created", "2025-01-18T06:24:39.968Z");
        filmProperties.put("edited", "2025-01-18T06:24:39.968Z");
        filmProperties.put("producer", "Gary Kurtz, Rick McCallum");
        filmProperties.put("title", "A New Hope");
        filmProperties.put("episode_id", 4);
        filmProperties.put("director", "George Lucas");
        filmProperties.put("release_date", "1977-05-25");
        filmProperties.put("opening_crawl", "It is a period of civil war...");
        filmProperties.put("url", "https://www.swapi.tech/api/films/1");

        FilmResponse response = FilmMapper.mapToFilmResponse(filmProperties);

        assertEquals("A New Hope", response.getTitle());
        assertEquals(4, response.getEpisodeId());
        assertEquals("George Lucas", response.getDirector());
        assertEquals("1977-05-25", response.getReleaseDate());
        assertEquals("It is a period of civil war...", response.getOpeningCrawl());
        assertEquals("https://www.swapi.tech/api/films/1", response.getUrl());
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/people/1"), filmProperties.get("characters"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/planets/1"), filmProperties.get("planets"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/starships/2"), filmProperties.get("starships"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/vehicles/4"), filmProperties.get("vehicles"));
        assertEquals(Collections.singletonList("https://www.swapi.tech/api/species/1"), filmProperties.get("species"));
        assertEquals("2025-01-18T06:24:39.968Z", filmProperties.get("created"));
        assertEquals("2025-01-18T06:24:39.968Z", filmProperties.get("edited"));
        assertEquals("Gary Kurtz, Rick McCallum", filmProperties.get("producer"));
        assertEquals("A New Hope", filmProperties.get("title"));
        assertEquals(4, filmProperties.get("episode_id"));
        assertEquals("George Lucas", filmProperties.get("director"));
        assertEquals("1977-05-25", filmProperties.get("release_date"));
        assertEquals("It is a period of civil war...", filmProperties.get("opening_crawl"));
        assertEquals("https://www.swapi.tech/api/films/1", filmProperties.get("url"));
        assertEquals(1, response.getCharacters().size());
        assertEquals("https://www.swapi.tech/api/people/1", response.getCharacters().get(0));
    }
}
