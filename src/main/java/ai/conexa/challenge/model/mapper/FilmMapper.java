package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.FilmResponse;

import java.util.List;
import java.util.Map;

public class FilmMapper extends BaseMapper{
    public static FilmResponse mapToFilmResponse(Map<String, Object> input) {
        return FilmResponse.builder()
                .characters((List<String>) input.get("characters"))
                .planets((List<String>) input.get("planets"))
                .starships((List<String>) input.get("starships"))
                .vehicles((List<String>) input.get("vehicles"))
                .species((List<String>) input.get("species"))
                .created((String) input.get("created"))
                .edited((String) input.get("edited"))
                .producer((String) input.get("producer"))
                .title((String) input.get("title"))
                .episodeId((Integer) input.get("episode_id"))
                .director((String) input.get("director"))
                .releaseDate((String) input.get("release_date"))
                .openingCrawl((String) input.get("opening_crawl"))
                .url((String) input.get("url"))
                .build();
    }
}
