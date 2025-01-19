package ai.conexa.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmResponse {
    private List<String> characters;
    private List<String> planets;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> species;
    private String created;
    private String edited;
    private String producer;
    private String title;
    private int episodeId;
    private String director;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("opening_crawl")
    private String openingCrawl;
    private String url;

}
