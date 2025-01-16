package ai.conexa.challenge.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private String uid;
    private String name;
    private String url;
}