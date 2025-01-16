package ai.conexa.challenge.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
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
    private String releaseDate;
    private String openingCrawl;
    private String url;

}
