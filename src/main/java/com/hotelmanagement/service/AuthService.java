package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.AuthRequest;
import com.hotelmanagement.dto.request.RegisterRequest;
import com.hotelmanagement.dto.response.AuthResponse;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.repository.UserRepository;
import com.hotelmanagement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getUsername());

        return login(new AuthRequest() {{
            setUsername(request.getUsername());
            setPassword(request.getPassword());
        }});
    }

    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtUtil.generateToken(authentication);
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            log.info("User logged in successfully: {}", request.getUsername());
            return new AuthResponse(token, user.getUsername(), user.getRole());

        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}