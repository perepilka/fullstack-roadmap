package com.perepilka.todo_backend.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void whenSaveTodo_thenFindById_returnsTodo() {

        Todo newTodo = new Todo();
        newTodo.setTitle("Test Todo Item");

        todoRepository.save(newTodo);

        Optional<Todo> foundTodo = todoRepository.findById(newTodo.getId());

        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get().getTitle()).isEqualTo("Test Todo Item");
        assertThat(foundTodo.get().getId()).isGreaterThan(0);
        assertThat(foundTodo.get().isCompleted()).isEqualTo(false);
        assertThat(foundTodo.get().getCreatedAt()).isNotNull();
        assertThat(foundTodo.get().getUpdatedAt()).isNotNull();

    }
}
