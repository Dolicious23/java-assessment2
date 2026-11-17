package com.example.clientmanagement.dto;

import java.util.List;

public record GeoResponse(
        String clientId,
        String clientName,
        String clientEmail,
        Long userId,
        String postTitle,
        String postBody,
        List<Comment> comments) {

    public record Comment(String name, String email, String body) {
    }
}
