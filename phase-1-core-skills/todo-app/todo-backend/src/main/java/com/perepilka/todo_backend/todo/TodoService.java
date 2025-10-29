package com.perepilka.todo_backend.todo;

import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Retrieves all Todo entities from the repository.
     * This method fetches and returns a list of all Todo objects
     * currently stored in the database.
     *
     * @return a list of all Todo entities
     */
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

}
