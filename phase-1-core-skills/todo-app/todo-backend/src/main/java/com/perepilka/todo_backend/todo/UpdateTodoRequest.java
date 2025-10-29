package com.perepilka.todo_backend.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateTodoRequest {

    @NotBlank(message = "Title cannot be blank!")
    @Size(max = 255)
    private String title;
    @NotNull(message = "Completed status cannot be null!")
    private Boolean completed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
