package com.perepilka.todo_backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perepilka.todo_backend.todo.TodoService;
import com.perepilka.todo_backend.user.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(JacksonAutoConfiguration.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void whenRegisters_withValidRequest_thenReturnsDto() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");

        AppUser savedUser = new AppUser();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setRole("ROLE_USER");

        when(authService.register(any(RegisterRequest.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void whenRegisters_withInvalidRequest_thenReturnsBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("a");
        registerRequest.setPassword("b");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void whenRegisters_withDuplicateUsername_thenReturnsConflict() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("duplicate");
        registerRequest.setPassword("password");

        when(authService.register(any(RegisterRequest.class))).thenThrow(new UsernameAlreadyExistsException("duplicate"));
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.message").value("Username already exists: duplicate"));


    }


}
