package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.Result;
import ai.conexa.challenge.model.response.StarshipResponse;
import ai.conexa.challenge.service.StarshipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class StarshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StarshipService starshipService;

    @Test
    public void testGetAllPaginated() throws Exception {
        Result result1 = Result.builder()
                .uid("1")
                .name("Millennium Falcon")
                .build();
        Result result2 = Result.builder()
                .uid("2")
                .name("X-Wing Starfighter")
                .build();

        List<Result> results = Arrays.asList(result1, result2);
        PaginatedResponse paginatedResponse = PaginatedResponse.builder().totalPages(1).totalRecords(2).results(results).build();

        when(starshipService.getAllPaginated(1, 2)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/starships?page=1&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].uid", is("1")))
                .andExpect(jsonPath("$.results[0].name", is("Millennium Falcon")))
                .andExpect(jsonPath("$.results[1].uid", is("2")))
                .andExpect(jsonPath("$.results[1].name", is("X-Wing Starfighter")));
    }

    @Test
    public void testGetAllPaginated_PageIsZero_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(get("/starships?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllPaginated_SizeIsZero_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(get("/starships?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetById() throws Exception {
        StarshipResponse starship = StarshipResponse.builder()
                .name("Slave 1")
                .model("Firespray-31-class patrol and attack")
                .manufacturer("Kuat Systems Engineering")
                .build();

        when(starshipService.getById(anyLong())).thenReturn(starship);

        mockMvc.perform(get("/starships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Slave 1"))
                .andExpect(jsonPath("$.model").value("Firespray-31-class patrol and attack"))
                .andExpect(jsonPath("$.manufacturer").value("Kuat Systems Engineering"));
    }

    @Test
    public void testGetByName() throws Exception {
        StarshipResponse starship = StarshipResponse.builder()
                .name("TIE Fighter")
                .model("TIE/ln starfighter")
                .manufacturer("Sienar Fleet Systems")
                .build();

        when(starshipService.getByName(anyString())).thenReturn(Collections.singletonList(starship));

        mockMvc.perform(get("/starships/?name=TIE Fighter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("TIE Fighter"))
                .andExpect(jsonPath("$[0].model").value("TIE/ln starfighter"))
                .andExpect(jsonPath("$[0].manufacturer").value("Sienar Fleet Systems"));
    }

    @Test
    public void testGetByName_noResults() throws Exception {
        when(starshipService.getByName(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/starships/?name=Death Star")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByName_nameIsNull_shouldThrowBadRequest() throws Exception {

        mockMvc.perform(get("/starships/?name=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
