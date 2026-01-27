package com.api.EcommerceBackend.user.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        String avatarUrl,

        @NotBlank String firstName,

        @NotBlank String lastName,

        @NotBlank @Email String email,

        @NotBlank @Size(min = 8, max = 72) String password,

        String cep) {
}
