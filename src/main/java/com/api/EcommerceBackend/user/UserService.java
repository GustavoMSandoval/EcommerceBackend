package com.api.EcommerceBackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.EcommerceBackend.user.dto.UserResponse;
import com.api.EcommerceBackend.user.dto.UserRequest;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    
    public ResponseEntity<UserResponse> uptade(UserRequest request){
        
        User user = userRepository.findById(request.id()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (request.avatarUrl() != null) user.setAvatarUrl(request.avatarUrl());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.password() != null) user.setPassword(request.password());
        if (request.cep() != null) user.setCep(request.cep());
        
    
        userRepository.save(user);

        return ResponseEntity.ok(new UserResponse());
  }
  
}
