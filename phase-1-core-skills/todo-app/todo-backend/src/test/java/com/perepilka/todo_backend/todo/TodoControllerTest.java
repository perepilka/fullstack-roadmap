package com.perepilka.todo_backend.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import(JacksonAutoConfiguration.class)
public class TodoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @MockitoBean
    private TodoService todoService;

    // Use constructor injection
    @Autowired
    public TodoControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @Test
    void whenGetTodoById_andTodoExists_thenReturnsTodo() throws Exception {

        Todo mockTodo = new Todo();
        mockTodo.setId(1L);
        mockTodo.setTitle("Test Todo");
        mockTodo.setCompleted(false);
        mockTodo.setCreatedAt(LocalDateTime.now());
        mockTodo.setUpdatedAt(LocalDateTime.now());

        when(todoService.getTodoById(mockTodo.getId())).thenReturn(mockTodo);

        mockMvc.perform(get("/api/v1/todos/{id}", mockTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockTodo.getId()))
                .andExpect(jsonPath("$.title").value(mockTodo.getTitle()))
                .andExpect(jsonPath("$.completed").value(mockTodo.isCompleted()));
    }


    @Test
    void whenGetTodoById_andTodoDoesNotExist_thenReturnsNotFound() throws Exception {
        long todoId = 404L;
        when(todoService.getTodoById(todoId)).thenThrow(new TodoNotFoundException(todoId));

        mockMvc.perform(get("/api/v1/todos/{id}", todoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenSaveTodo_withValidRequest_thenReturns() throws Exception {

        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setTitle("Test Todo");

        Todo createdTodo = new Todo();
        createdTodo.setId(1L);
        createdTodo.setTitle("Test Todo");
        createdTodo.setCompleted(false);

        when(todoService.createTodo(any(CreateTodoRequest.class))).thenReturn(createdTodo);

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTodoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void whenSaveTodo_withInvalidRequest_thenReturnsBadRequest() throws Exception {

        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setTitle(""); // Invalid title (blank)

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTodoRequest)))
                .andExpect(status().isBadRequest()); // Expect HTTP 400

    }

}
