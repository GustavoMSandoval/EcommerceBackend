package com.api.EcommerceBackend.user.dto;

import java.time.Instant;
import java.util.UUID;

import javax.management.relation.Role;

public record UserResponse(UUID id, 
                            String avatarUrl,
                            String firstName,
                            String lastName,
                            String email,
                            String password,
                            String cep,
                            Role role,
                            Instant createdAt,
                            Instant uptadeAt) {
    
}
