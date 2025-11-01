package com.perepilka.todo_backend.auth;

import com.perepilka.todo_backend.user.AppUser;
import com.perepilka.todo_backend.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void whenRegister_withValidUser_thenSavesUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");

        String HASHED_PASSWORD = "hashed_password";
        when(passwordEncoder.encode("password123")).thenReturn(HASHED_PASSWORD);

        AppUser mockSavedUser = new AppUser();
        mockSavedUser.setUsername("testuser");
        mockSavedUser.setRole(HASHED_PASSWORD);
        mockSavedUser.setRole("ROLE_USER");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(mockSavedUser);

        AppUser savedUser = authService.register(registerRequest);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getPassword()).isEqualTo(HASHED_PASSWORD); // Verify it's hashed
        assertThat(savedUser.getRole()).isEqualTo("ROLE_USER");

        verify(passwordEncoder).encode("password123");

        verify(appUserRepository).save(argThat(user ->
                user.getUsername().equals("testuser") &&
                        user.getPassword().equals(HASHED_PASSWORD) &&
                        user.getRole().equals("ROLE_USER")
        ));
    }

    @Test
    void whenRegister_withDuplicateUsername_thenThrowsUsernameAlreadyExistsException() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("duplicate");
        request.setPassword("password123");

        // Mock the repository to throw the DB-level exception
        when(appUserRepository.save(any(AppUser.class)))
                .thenThrow(new DataIntegrityViolationException("... unique constraint ..."));

        // Act & Assert
        // Verify our service catches the DB exception and throws our custom one
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            authService.register(request);
        });
    }

}
