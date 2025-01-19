package ai.conexa.challenge.model.generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PaginatedResult {
    private String uid;
    private String name;
    private String url;
}