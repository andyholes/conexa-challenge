package ai.conexa.challenge.service;

import ai.conexa.challenge.exception.ResourceNotFoundException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

public interface SwapiClient {
    /**
     * Fetches paginated data from the given URL.
     *
     * @param url the URL to fetch data from
     * @return a map containing the paginated data
     */
    public Map<String, Object> fetchPaginatedData(String url);

    /**
     * Fetches a list of results from the given URL.
     *
     * @param url the URL to fetch data from
     * @return a list of maps containing the results
     */
    public List<Map<String, Object>> fetchResults(String url);

    /**
     * Fetches a single result from the given URL.
     *
     * @param url the URL to fetch data from
     * @return a map containing the single result
     * @throws ResourceNotFoundException if the resource is not found
     * @throws RestClientException       if there is an error fetching the data
     */
    public Map<String, Object> fetchSingleResult(String url);
}