package com.perepilka.todo_backend.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private TodoRepository todoRepository;

    TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Finds a todo by its ID
     * @param id The ID of the todo to find
     * @return The found Todo entity
     * @throws TodoNotFoundException if the todo is not found
     */
    public Todo getTodoById(long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

}
