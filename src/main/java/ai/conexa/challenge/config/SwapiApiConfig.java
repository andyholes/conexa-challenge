package ai.conexa.challenge.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Configuration class for SWAPI API endpoints.
 * This class holds the URLs for various SWAPI API endpoints and logs them upon initialization.
 * The URLs are set in the swapi-endpoints.yaml file and can be modified via environment variables.
 */
@Component
@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "swapi.endpoints")
public class SwapiApiConfig {
    private String peoplePaginated;
    private String peopleById;
    private String peopleByName;
    private String vehiclesPaginated;
    private String vehiclesById;
    private String vehiclesByName;
    private String starshipsPaginated;
    private String starshipsById;
    private String starshipsByName;
    private String filmsAll;
    private String filmsById;
    private String filmsByTitle;

    /**
     * Logs the currently available SWAPI API URLs.
     * This method is called after the properties are set for debugging purposes.
     */
    @PostConstruct
    void logUrls() {
        log.info("Currently available Swapi API URLs:");
        log.info("PeoplePaginated URL: " + peoplePaginated);
        log.info("PeopleById URL: " + peopleById);
        log.info("PeopleByName URL: " + peopleByName);
        log.info("VehiclesPaginated URL: " + vehiclesPaginated);
        log.info("VehiclesById URL: " + vehiclesById);
        log.info("VehiclesByName URL: " + vehiclesByName);
        log.info("StarshipsPaginated URL: " + starshipsPaginated);
        log.info("StarshipsById URL: " + starshipsById);
        log.info("StarshipsByName URL: " + starshipsByName);
        log.info("FilmsAll URL: " + filmsAll);
        log.info("FilmsById URL: " + filmsById);
        log.info("FilmsByTitle URL: " + filmsByTitle);
    }
}