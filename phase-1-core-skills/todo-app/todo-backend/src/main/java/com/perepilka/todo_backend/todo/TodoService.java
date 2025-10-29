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

    /**
     * Updates an existing Todo entity.
     * This method retrieves a Todo by its ID, updates its title and completion status
     * based on the provided UpdateTodoRequest, and saves the changes to the repository.
     *
     * @param id the ID of the Todo to update
     * @param updateTodoRequest request object containing the updated title and completion status
     * @return the updated Todo entity
     * @throws TodoNotFoundException if the Todo with the specified ID does not exist
     */
    public Todo updateTodo(Long id, UpdateTodoRequest updateTodoRequest) {
        //throws exception if todo does not exist
        Todo todoToUpdate = getTodoById(id);

        todoToUpdate.setTitle(updateTodoRequest.getTitle());
        todoToUpdate.setCompleted(updateTodoRequest.getCompleted());

        return todoRepository.save(todoToUpdate);
    }

    public void deleteTodo(Long id) {
        getTodoById(id);
        todoRepository.deleteById(id);
    }

}
