package ai.conexa.challenge.controller;

import ai.conexa.challenge.model.response.FilmResponse;
import ai.conexa.challenge.service.FilmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Test
    public void testGetFilms() throws Exception {
        FilmResponse film1 = FilmResponse.builder()
                .title("A New Hope")
                .director("George Lucas")
                .build();
        FilmResponse film2 = FilmResponse.builder()
                .title("The Empire Strikes Back")
                .director("Irvin Kershner")
                .build();
        List<FilmResponse> films = Arrays.asList(film1, film2);

        when(filmService.getAll()).thenReturn(films);

        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("A New Hope")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].director", is("George Lucas")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("The Empire Strikes Back")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].director", is("Irvin Kershner")));
    }

    @Test
    public void getFilmsById() throws Exception {
        FilmResponse film = FilmResponse.builder()
                .title("Return of the Jedi")
                .director("Richard Marquand")
                .build();

        when(filmService.getById(anyLong())).thenReturn(film);

        mockMvc.perform(get("/films/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Return of the Jedi"))
                .andExpect(jsonPath("$.director").value("Richard Marquand"));
    }

    @Test
    void getFilmsByTitle() throws Exception {
        FilmResponse film = FilmResponse.builder()
                .title("The Force Awakens")
                .director("J.J. Abrams")
                .build();

        when(filmService.getByTitle(anyString())).thenReturn(Collections.singletonList(film));

        mockMvc.perform(get("/films/?title=The Force Awakens")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("The Force Awakens"))
                .andExpect(jsonPath("$[0].director").value("J.J. Abrams"));
    }

    @Test
    void getFilmsByTitle_filmDontExists() throws Exception {
        when(filmService.getByTitle(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/films/?title=The Last Jedi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByTitle_titleIsNull_shouldThrowBadRequest() throws Exception {

        mockMvc.perform(get("/films/?title=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}