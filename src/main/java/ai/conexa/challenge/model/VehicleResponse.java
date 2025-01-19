package ai.conexa.challenge.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VehicleResponse {
    private String model;
    private String vehicleClass;
    private String manufacturer;
    private String costInCredits;
    private String length;
    private String crew;
    private String passengers;
    private String maxAtmospheringSpeed;
    private String cargoCapacity;
    private String consumables;
    private List<String> films;
    private List<String> pilots;
    private String created;
    private String edited;
    private String name;
    private String url;

}
