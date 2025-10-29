package com.perepilka.todo_backend.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void whenGetTodoById_andTodoExists_thenReturnsTodo() {
        Todo newTodo = new Todo();
        newTodo.setId(1L);
        newTodo.setTitle("title");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(newTodo));

        Todo foundTodo = todoService.getTodoById(1L);

        assertThat(foundTodo).isNotNull();
        assertThat(foundTodo.getTitle()).isEqualTo(newTodo.getTitle());
    }

    @Test
    void whenGetTodoById_andTodoNotExists_thenThrowsException() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class,
                () -> {
                    todoService.getTodoById(404L);
                });
    }

    @Test
    void whenSaveTodo_thenSavesAndReturnsTodo() {
        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setTitle("Todo Title");

        Todo mockTodo = new Todo();
        mockTodo.setId(1L);
        mockTodo.setTitle("Todo Title");

        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);

        Todo createdTodo = todoService.createTodo(createTodoRequest);

        assertThat(createdTodo).isNotNull();
        assertThat(createdTodo.getId()).isEqualTo(1L);
        assertThat(createdTodo.getTitle()).isEqualTo("Todo Title");

        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void whenGetTodos_thenReturnsListOfTodos() {

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Todo 1");

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Todo 2");

        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        List<Todo> todos = todoService.getAllTodos();

        assertThat(todos).isNotNull();
        assertThat(todos.size()).isEqualTo(2);
        assertThat(todos.get(0).getTitle()).isEqualTo("Todo 1");

        verify(todoRepository).findAll();
    }

    @Test
    void whenUpdateTodo_andTodoExists_thenUpdatesAndReturnsTodo() {
        Long todoId = 1L;
        UpdateTodoRequest updateTodoRequest = new UpdateTodoRequest();
        updateTodoRequest.setTitle("Updated Title");
        updateTodoRequest.setCompleted(true);
        Todo existingTodo = new Todo();
        existingTodo.setId(todoId);
        existingTodo.setTitle("Old Title");
        existingTodo.setCompleted(false);

        Todo savedTodo = new Todo();
        savedTodo.setId(todoId);
        savedTodo.setTitle("Updated Title");
        savedTodo.setCompleted(true);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        Todo updatedTodo = todoService.updateTodo(todoId, updateTodoRequest);

        assertThat(updatedTodo).isNotNull();
        assertThat(updatedTodo.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTodo.isCompleted()).isTrue();

        verify(todoRepository).save(existingTodo);
    }

    @Test
    void whenUpdateTodo_andTodoDoesNotExist_thenThrowException() {
        Long todoId = 1L;
        UpdateTodoRequest updateTodoRequest = new UpdateTodoRequest();
        updateTodoRequest.setTitle("Updated Title");
        updateTodoRequest.setCompleted(true);

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () ->
                todoService.updateTodo(todoId, updateTodoRequest));

        verify(todoRepository, never()).save(any(Todo.class));
    }

}


