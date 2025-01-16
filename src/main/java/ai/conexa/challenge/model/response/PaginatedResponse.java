package ai.conexa.challenge.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse {
    private int totalRecords;
    private int totalPages;
    private List<Result> results;
}
