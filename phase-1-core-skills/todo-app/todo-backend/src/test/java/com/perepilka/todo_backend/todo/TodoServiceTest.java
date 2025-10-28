package com.perepilka.todo_backend.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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


}


