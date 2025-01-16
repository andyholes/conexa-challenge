package ai.conexa.challenge.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StarshipResponse {
    private String model;
    private String starshipClass;
    private String manufacturer;
    private String costInCredits;
    private String length;
    private String crew;
    private String passengers;
    private String maxAtmospheringSpeed;
    private String hyperdriveRating;
    private String MGLT;
    private String cargoCapacity;
    private String consumables;
    private List<String> pilots;
    private String created;
    private String edited;
    private String name;
    private String url;

}
