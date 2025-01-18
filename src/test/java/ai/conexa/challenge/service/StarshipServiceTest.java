package ai.conexa.challenge.service;

import ai.conexa.challenge.controller.StarshipController;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.StarshipResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class StarshipServiceTest {

    @Mock
    private StarshipService starshipService;

    @InjectMocks
    private StarshipController starshipController;

    @Test
    void getAllPaginatedReturnsPaginatedListOfStarships() {
        PaginatedResponse paginatedResponse = PaginatedResponse.builder().build();
        when(starshipService.getAllPaginated(1, 10)).thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponse> response = starshipController.getAllPaginated(1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(paginatedResponse, response.getBody());
    }

    @Test
    void getAllPaginatedThrowsExceptionForInvalidPage() {
        assertThrows(ConstraintViolationException.class, () -> starshipController.getAllPaginated(0, 10));
    }

    @Test
    void getAllPaginatedThrowsExceptionForInvalidSize() {
        assertThrows(ConstraintViolationException.class, () -> starshipController.getAllPaginated(1, 0));
    }

    @Test
    void getAllPaginatedReturnsEmptyListForNoStarships() {
        PaginatedResponse paginatedResponse = PaginatedResponse.builder().build();
        when(starshipService.getAllPaginated(1, 10)).thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponse> response = starshipController.getAllPaginated(1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(paginatedResponse, response.getBody());
    }

    @Test
    void getByIdReturnsStarshipById() {
        StarshipResponse starshipResponse = StarshipResponse.builder().build();
        when(starshipService.getById(1L)).thenReturn(starshipResponse);

        ResponseEntity<StarshipResponse> response = starshipController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(starshipResponse, response.getBody());
    }

    @Test
    void getByIdThrowsExceptionForNonExistentId() {
        when(starshipService.getById(999L)).thenThrow(new RuntimeException("Starship not found"));

        assertThrows(RuntimeException.class, () -> starshipController.getById(999L));
    }

    @Test
    void getByIdThrowsExceptionForInvalidId() {
        when(starshipService.getById(-1L)).thenThrow(new ConstraintViolationException("Invalid ID", null));

        assertThrows(ConstraintViolationException.class, () -> starshipController.getById(-1L));
    }

    @Test
    void getByNameReturnsStarshipsByName() {
        List<StarshipResponse> starshipResponses = Collections.singletonList(StarshipResponse.builder().build());
        when(starshipService.getByName("Falcon")).thenReturn(starshipResponses);

        ResponseEntity<List<StarshipResponse>> response = starshipController.getByName("Falcon");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(starshipResponses, response.getBody());
    }

    @Test
    void getByNameThrowsExceptionForBlankName() {
        assertThrows(ConstraintViolationException.class, () -> starshipController.getByName(""));
    }

    @Test
    void getByNameReturnsEmptyListForNonExistentName() {
        when(starshipService.getByName("NonExistent")).thenReturn(Collections.emptyList());

        ResponseEntity<List<StarshipResponse>> response = starshipController.getByName("NonExistent");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void getByNameThrowsExceptionForNullName() {
        assertThrows(ConstraintViolationException.class, () -> starshipController.getByName(null));
    }

    @Test
    void getByNameReturnsStarshipsForPartialName() {
        List<StarshipResponse> starshipResponses = Collections.singletonList(StarshipResponse.builder().build());
        when(starshipService.getByName("Fal")).thenReturn(starshipResponses);

        ResponseEntity<List<StarshipResponse>> response = starshipController.getByName("Fal");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(starshipResponses, response.getBody());
    }

    @Test
    void getByNameReturnsMultipleStarshipsForCommonName() {
        List<StarshipResponse> starshipResponses = Arrays.asList(StarshipResponse.builder().build(), StarshipResponse.builder().build());
        when(starshipService.getByName("Star")).thenReturn(starshipResponses);

        ResponseEntity<List<StarshipResponse>> response = starshipController.getByName("Star");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(starshipResponses, response.getBody());
    }
}