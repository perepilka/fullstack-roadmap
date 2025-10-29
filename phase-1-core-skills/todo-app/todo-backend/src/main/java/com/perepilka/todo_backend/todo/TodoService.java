package com.perepilka.todo_backend.todo;

import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Finds a todo by its ID
     * @param id The ID of the todo to find
     * @return The found Todo entity
     * @throws TodoNotFoundException if the todo is not found
     */
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    /**
     * Creates and saves a new Todo entity.
     * This method constructs a new Todo from the supplied CreateTodoRequest,
     * sets its title, and persists it using the TodoRepository.
     *
     * @param createTodoRequest request object containing data for the new todo (must provide a title)
     * @return the persisted Todo entity with any generated fields populated (e.g. id)
     */
    public Todo createTodo(CreateTodoRequest createTodoRequest) {
        Todo todo = new Todo();
        todo.setTitle(createTodoRequest.getTitle());
        return todoRepository.save(todo);
    }

}
