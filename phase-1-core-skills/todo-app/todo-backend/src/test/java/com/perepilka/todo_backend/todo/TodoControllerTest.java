package com.perepilka.todo_backend.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

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

}
