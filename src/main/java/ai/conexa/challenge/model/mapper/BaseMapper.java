package ai.conexa.challenge.model.mapper;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.Result;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseMapper {
    public static PaginatedResponse mapToPaginatedResponse(Map<String, Object> jsonString) {
        return PaginatedResponse.builder()
                .totalRecords((Integer) jsonString.get("total_records"))
                .totalPages((Integer) jsonString.get("total_pages"))
                .results(mapGeneralResult((List<Map<String, Object>>) jsonString.get("results")))
                .build();
    }

    private static List<Result> mapGeneralResult(List<Map<String, Object>> results) {
        return results.stream().map(
                result -> Result.builder()
                    .uid((String) result.get("uid"))
                    .name((String) result.get("name"))
                    .url((String) result.get("url"))
                .build()
        ).collect(Collectors.toList());
    }
}