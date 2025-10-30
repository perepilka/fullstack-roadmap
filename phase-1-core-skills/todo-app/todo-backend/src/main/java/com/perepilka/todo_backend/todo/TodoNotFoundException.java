package com.perepilka.todo_backend.todo;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Could not find todo with id: " + id);
    }
}
