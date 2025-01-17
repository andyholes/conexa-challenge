package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.PeopleResponse;

import java.util.List;
import java.util.Map;

public class PeopleMapper extends BaseMapper{

    public static PeopleResponse mapToPeopleResponse(Map<String, Object> properties) {
        return PeopleResponse.builder()
                .name((String) properties.get("name"))
                .birthYear((String) properties.get("birth_year"))
                .eyeColor((String) properties.get("eye_color"))
                .gender((String) properties.get("gender"))
                .hairColor((String) properties.get("hair_color"))
                .height((String) properties.get("height"))
                .mass((String) properties.get("mass"))
                .skinColor((String) properties.get("skin_color"))
                .homeworld((String) properties.get("homeworld"))
                .films((List<String>) properties.get("films"))
                .species((List<String>) properties.get("species"))
                .starships((List<String>) properties.get("starships"))
                .vehicles((List<String>) properties.get("vehicles"))
                .url((String) properties.get("url"))
                .created((String) properties.get("created"))
                .edited((String) properties.get("edited"))
                .build();
    }
}
