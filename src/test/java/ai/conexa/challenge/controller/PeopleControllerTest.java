package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.PeopleResponse;
import ai.conexa.challenge.model.response.Result;
import ai.conexa.challenge.service.PeopleService;
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
class PeopleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeopleService peopleService;

    @Test
    public void testGetAllPaginated() throws Exception {
        Result result1 = Result.builder()
                .uid("1")
                .name("Luke Skywalker")
                .build();
        Result result2 = Result.builder()
                .uid("2")
                .name("Darth Vader")
                .build();

        List<Result> results = Arrays.asList(result1, result2);
        PaginatedResponse paginatedResponse = PaginatedResponse.builder().totalPages(1).totalRecords(2).results(results).build();

        when(peopleService.getAllPaginated(1, 2)).thenReturn(paginatedResponse);

        mockMvc.perform(get("/people?page=1&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].uid", is("1")))
                .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")))
                .andExpect(jsonPath("$.results[1].uid", is("2")))
                .andExpect(jsonPath("$.results[1].name", is("Darth Vader")));
    }

    @Test
    public void testGetAllPaginated_PageIsZero_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(get("/people?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllPaginated_SizeIsZero_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(get("/people?page=1&size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testGetById() throws Exception {
        PeopleResponse person = PeopleResponse.builder()
                .name("Leia Organa")
                .birthYear("1234")
                .build();

        when(peopleService.getById(anyLong())).thenReturn(person);

        mockMvc.perform(get("/people/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Leia Organa"))
                .andExpect(jsonPath("$.birthYear").value("1234"));
    }

    @Test
    public void testGetByName() throws Exception {
        PeopleResponse person = PeopleResponse.builder()
                .name("Han Solo")
                .birthYear("1234")
                .build();

        when(peopleService.getByName(anyString())).thenReturn(Collections.singletonList(person));

        mockMvc.perform(get("/people/?name=Han")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Han Solo"))
                .andExpect(jsonPath("$[0].birthYear").value("1234"));
    }

    @Test
    public void testGetByName_noResults() throws Exception {
        when(peopleService.getByName(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/people/?name=Chewbacca")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByName_nameIsNull_shouldThrowBadRequest() throws Exception {

        mockMvc.perform(get("/people/?name=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}