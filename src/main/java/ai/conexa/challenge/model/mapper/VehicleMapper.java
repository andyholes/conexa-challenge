package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.VehicleResponse;

import java.util.List;
import java.util.Map;

public class VehicleMapper extends BaseMapper{
    public static VehicleResponse mapToVehicleResponse(Map<String, Object> input) {
        return VehicleResponse.builder()
                .model((String) input.get("model"))
                .vehicleClass((String) input.get("vehicle_class"))
                .manufacturer((String) input.get("manufacturer"))
                .costInCredits((String) input.get("cost_in_credits"))
                .length((String) input.get("length"))
                .crew((String) input.get("crew"))
                .passengers((String) input.get("passengers"))
                .maxAtmospheringSpeed((String) input.get("max_atmosphering_speed"))
                .cargoCapacity((String) input.get("cargo_capacity"))
                .consumables((String) input.get("consumables"))
                .films((List<String>) input.get("films"))
                .pilots((List<String>) input.get("pilots"))
                .created((String) input.get("created"))
                .edited((String) input.get("edited"))
                .name((String) input.get("name"))
                .url((String) input.get("url"))
                .build();
    }
}
