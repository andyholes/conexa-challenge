package ai.conexa.challenge.service;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Interface for SWAPI client.
 * This interface defines methods for fetching objects from the SWAPI API.
 */
public interface SwapiClient {

    /**
     * Fetches an object from the given URL using the specified response type.
     *
     * @param url          the URL to fetch the object from
     * @param responseType the class type of the response
     *                     (e.g., <b>PaginatedResponse.class, SingleResultResponse.class</b>)
     * @return the fetched object of type T
     */
    <T> T fetchObject(String url, Class<T> responseType);

    /**
     * Fetches an object from the given URL using the specified TypeReference.
     *
     * @param url           the URL to fetch the object from
     * @param typeReference the TypeReference for the response
     *                      (e.g., <b>new TypeReference&lt;SingleResultResponse&lt;PeopleResponse&gt;&gt;() {}</b>)
     * @return the fetched object of type T
     */
    <T> T fetchObject(String url, TypeReference<T> typeReference);
}