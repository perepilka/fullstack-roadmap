package com.perepilka.todo_backend.todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
