package com.api.EcommerceBackend.user.auth;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.EcommerceBackend.user.User;
import com.api.EcommerceBackend.user.UserRepository;
import com.api.EcommerceBackend.user.auth.dto.LoginRequest;
import com.api.EcommerceBackend.user.auth.dto.RegisterRequest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return jwtService.generateToken(user);
    }


    public void register(RegisterRequest request) {        

        User user = new User(
                request.avatarUrl(),
                request.firstName(),
                request.lastName(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.cep()
        );

        userRepository.save(user);

    }
    
}
