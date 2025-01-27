package ai.conexa.challenge.unit.security;

import ai.conexa.challenge.security.JwtUtils;
import ai.conexa.challenge.security.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static ai.conexa.challenge.util.MessageConstants.INVALID_CREDENTIALS;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin_Success() throws Exception {
        UserRequest userRequest = new UserRequest("admin", "1234");
        when(jwtUtils.generateToken("admin")).thenReturn("test-token");

        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("Bearer test-token"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        UserRequest userRequest = new UserRequest("admin", "wrong-password");

        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(INVALID_CREDENTIALS));
    }

    @Test
    void testLogin_BadRequest() throws Exception {
        UserRequest userRequest = new UserRequest("", "");

        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_InternalServerError() throws Exception {
        UserRequest userRequest = new UserRequest("admin", "1234");
        when(jwtUtils.generateToken("admin")).thenThrow(new RuntimeException("Internal error"));

        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal error"));
    }
}