package ai.conexa.challenge.model.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"total_records", "total_pages", "results"})
public class PaginatedResponse {
    @JsonProperty("total_records")
    private int totalRecords;
    @JsonProperty("total_pages")
    private int totalPages;

    private List<PaginatedResult> results;
}
