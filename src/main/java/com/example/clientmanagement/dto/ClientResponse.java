package com.example.clientmanagement.dto;

import com.example.clientmanagement.domain.Client;

import java.time.LocalDateTime;

public record ClientResponse(
        Long id,
        String name,
        String email,
        String phone,
        String company,
        String city,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getCompany(),
                client.getCity(),
                client.getCreatedAt(),
                client.getUpdatedAt());
    }
}
