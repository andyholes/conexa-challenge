package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.StarshipResponse;

import java.util.List;
import java.util.Map;

public class StarshipMapper extends BaseMapper{
    public static StarshipResponse mapToStarshipResponse(Map<String, Object> input) {
        return StarshipResponse.builder()
                .model((String) input.get("model"))
                .starshipClass((String) input.get("starship_class"))
                .manufacturer((String) input.get("manufacturer"))
                .costInCredits((String) input.get("cost_in_credits"))
                .length((String) input.get("length"))
                .crew((String) input.get("crew"))
                .passengers((String) input.get("passengers"))
                .maxAtmospheringSpeed((String) input.get("max_atmosphering_speed"))
                .hyperdriveRating((String) input.get("hyperdrive_rating"))
                .MGLT((String) input.get("MGLT"))
                .cargoCapacity((String) input.get("cargo_capacity"))
                .consumables((String) input.get("consumables"))
                .pilots((List<String>) input.get("pilots"))
                .created((String) input.get("created"))
                .edited((String) input.get("edited"))
                .name((String) input.get("name"))
                .url((String) input.get("url"))
                .build();
    }
}
