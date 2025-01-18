package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.Result;
import ai.conexa.challenge.model.response.VehicleResponse;
import ai.conexa.challenge.service.VehicleService;
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
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Test
    public void testGetAllPaginated() throws Exception {
        Result result1 = Result.builder()
                .uid("1")
                .name("Speeder Bike")
                .build();
        Result result2 = Result.builder()
                .uid("2")
                .name("TIE Fighter")
                .build();

        List<Result> results = Arrays.asList(result1, result2);
        PaginatedResponse paginatedResponse = PaginatedResponse.builder().totalPages(1).totalRecords(2).results(results).build();

        when(vehicleService.getAllPaginated(1, 2)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/vehicles?page=1&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].uid", is("1")))
                .andExpect(jsonPath("$.results[0].name", is("Speeder Bike")))
                .andExpect(jsonPath("$.results[1].uid", is("2")))
                .andExpect(jsonPath("$.results[1].name", is("TIE Fighter")));
    }

    @Test
    public void testGetAllPaginated_Page_IsZero_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(get("/vehicles?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllPaginated_SizeIsZero_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(get("/vehicles?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetById() throws Exception {
        VehicleResponse vehicle = VehicleResponse.builder()
                .name("X-Wing")
                .model("T-65 X-wing starfighter")
                .manufacturer("Incom Corporation")
                .build();

        when(vehicleService.getById(anyLong())).thenReturn(vehicle);

        mockMvc.perform(get("/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("X-Wing"))
                .andExpect(jsonPath("$.model").value("T-65 X-wing starfighter"))
                .andExpect(jsonPath("$.manufacturer").value("Incom Corporation"));
    }

    @Test
    public void testGetByName() throws Exception {
        VehicleResponse vehicle = VehicleResponse.builder()
                .name("AT-AT")
                .model("All Terrain Armored Transport")
                .manufacturer("Kuat Drive Yards")
                .build();

        when(vehicleService.getByName(anyString())).thenReturn(Collections.singletonList(vehicle));

        mockMvc.perform(get("/vehicles/?name=AT-AT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("AT-AT"))
                .andExpect(jsonPath("$[0].model").value("All Terrain Armored Transport"))
                .andExpect(jsonPath("$[0].manufacturer").value("Kuat Drive Yards"));
    }

    @Test
    public void testGetByName_noResults() throws Exception {
        when(vehicleService.getByName(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vehicles/?name=Millennium Falcon")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByName_nameIsNull_shouldThrowBadRequest() throws Exception {

        mockMvc.perform(get("/vehicles/?name=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
