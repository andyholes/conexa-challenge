package ai.conexa.challenge.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PeopleResponse {
    private String name;
    private String birthYear;
    private String eyeColor;
    private String gender;
    private String hairColor;
    private String height;
    private String mass;
    private String skinColor;
    private String homeworld;
    private List<String> films;
    private List<String> species;
    private List<String> starships;
    private List<String> vehicles;
    private String url;
    private String created;
    private String edited;


}
