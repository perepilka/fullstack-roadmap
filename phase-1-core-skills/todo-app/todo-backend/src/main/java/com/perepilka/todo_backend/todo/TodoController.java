package com.perepilka.todo_backend.todo;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Handles HTTP GET requests to retrieve a Todo item by its ID.
     *
     * @param id the ID of the Todo item to retrieve, provided as a path variable
     * @return a ResponseEntity containing the TodoDto representation of the Todo item
     *         if found, or an appropriate HTTP status code if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id){

        Todo todo = todoService.getTodoById(id);

        return ResponseEntity.ok(TodoDto.fromEntity(todo));

    }

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@Valid @RequestBody CreateTodoRequest createTodoRequest) {
        Todo savedTodo = todoService.createTodo(createTodoRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTodo.getId())
                .toUri();

        return ResponseEntity.created(location).body(TodoDto.fromEntity(savedTodo));
    }

}
