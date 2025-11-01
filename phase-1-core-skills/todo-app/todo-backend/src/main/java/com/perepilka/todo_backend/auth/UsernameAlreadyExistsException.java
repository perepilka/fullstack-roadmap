package com.perepilka.todo_backend.auth;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("User with this username already exists: " + username);
    }
}
