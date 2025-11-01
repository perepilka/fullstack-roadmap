package com.perepilka.todo_backend.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppUserRepositoryTest {

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
    private AppUserRepository appUserRepository;

    private AppUser createAppUser(String username) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword("password");
        appUser.setRole("ROLE_USER");
        return appUserRepository.save(appUser);
    }

    @Test
    void whenFindByUsername_andUserExists_thenReturnUser() {
        createAppUser("testuser");

        Optional<AppUser> foundUser = appUserRepository.findByUsername("testuser");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void whenFindByUsername_andUserDoesNotExist_thenReturnEmpty() {
        Optional<AppUser> foundUser = appUserRepository.findByUsername("nonexistent");

        assertThat(foundUser).isEmpty();
    }

    @Test
    void whenSaveUser_andUsernameExists_thenThrowException() {
        createAppUser("duplicateuser");

        AppUser duplicateUser = new AppUser();
        duplicateUser.setUsername("duplicateuser");
        duplicateUser.setPassword("password");
        duplicateUser.setRole("ROLE_USER");

        assertThrows(DataIntegrityViolationException.class, () ->
                appUserRepository.save(duplicateUser));
    }


}
