package com.api.EcommerceBackend.user.auth;


import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.EcommerceBackend.infra.security.TokenService;
import com.api.EcommerceBackend.user.User;
import com.api.EcommerceBackend.user.UserRepository;
import com.api.EcommerceBackend.user.auth.dto.LoginRequest;
import com.api.EcommerceBackend.user.auth.dto.LoginResponse;
import com.api.EcommerceBackend.user.auth.dto.RegisterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    

    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponse(user.getFirstName(), token));
        }

        return ResponseEntity.badRequest().build();
    }


    public ResponseEntity<LoginResponse> register(RegisterRequest request) {        

        Optional<User> findEmail = this.userRepository.findByEmail(request.email());

        if(findEmail.isEmpty()) {
            User user = new User(
                    request.avatarUrl(),
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    passwordEncoder.encode(request.password()),
                    request.cep()
            );

            userRepository.save(user);

            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponse(user.getFirstName(), token));
        }

        return ResponseEntity.badRequest().build();

    }
    
}
