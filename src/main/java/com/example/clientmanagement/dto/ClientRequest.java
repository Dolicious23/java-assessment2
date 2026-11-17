package com.example.clientmanagement.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record ClientRequest(
        @NotBlank(message = "name is required")
        @Size(max = 150, message = "name must not exceed 150 characters")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid address")
        @Size(max = 200, message = "email must not exceed 200 characters")
        String email,

        @Size(max = 30, message = "phone must not exceed 30 characters")
        String phone,

        @Size(max = 150, message = "company must not exceed 150 characters")
        String company,

        @Size(max = 100, message = "city must not exceed 100 characters")
        String city) {
}
