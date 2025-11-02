package com.perepilka.todo_backend.auth;

import com.perepilka.todo_backend.user.AppUser;
import com.perepilka.todo_backend.user.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AppUser savedUSer =  authService.register(registerRequest);

        return new ResponseEntity<>(UserDto.fromEntity(savedUSer), HttpStatus.CREATED);
    }
}
