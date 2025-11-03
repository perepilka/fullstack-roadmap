package com.perepilka.todo_backend.auth;

import com.perepilka.todo_backend.jwt.JwtService;
import com.perepilka.todo_backend.user.AppUser;
import com.perepilka.todo_backend.user.AppUserRepository;
import com.perepilka.todo_backend.user.UserDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AppUser register(RegisterRequest request) {
        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("ROLE_USER");

        try {
            return appUserRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
    }
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after authentication"));

        String token = jwtService.generateToken(user);

        UserDto userDto = UserDto.fromEntity(user);
        return new AuthResponse(token, userDto);
    }
}
