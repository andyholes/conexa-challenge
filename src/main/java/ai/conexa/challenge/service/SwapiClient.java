package ai.conexa.challenge.service;

import java.util.List;
import java.util.Map;

public interface SwapiClient {
    public Map<String, Object> fetchPaginatedData(String url);
    public List<Map<String, Object>> fetchResults(String url);
    public Map<String, Object> fetchSingleResult(String url);
}