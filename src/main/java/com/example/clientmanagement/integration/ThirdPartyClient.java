package com.example.clientmanagement.integration;

import com.example.clientmanagement.config.ThirdPartyProperties;
import com.example.clientmanagement.dto.GeoResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ThirdPartyClient {

    private static final Logger log = LoggerFactory.getLogger(ThirdPartyClient.class);

    private final RestTemplate restTemplate;
    private final ThirdPartyProperties properties;

    public ThirdPartyClient(RestTemplate restTemplate, ThirdPartyProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public GeoResponse fetchProfile(String clientId, String clientName, String clientEmail, Long userId) {
        String base = properties.getGeoBaseUrl();

        log.info("Nested call start base={} userId={}", base, userId);
        Post post = execute(base + "/posts/{id}", "post", userId);
        List<Comment> comments = executeList(base + "/posts/{id}/comments", "comments", userId);

        return new GeoResponse(
                clientId,
                clientName,
                clientEmail,
                userId,
                post == null ? null : post.getTitle(),
                post == null ? null : post.getBody(),
                comments.stream()
                        .limit(3)
                        .map(c -> new GeoResponse.Comment(c.getName(), c.getEmail(), c.getBody()))
                        .collect(Collectors.toList()));
    }

    private Post execute(String url, String label, Long id) {
        ResponseEntity<Post> response = restTemplate.getForEntity(url, Post.class, id);
        log.info("Third-party {} status={} body={}", label, response.getStatusCodeValue(), response.getBody());
        return response.getBody();
    }

    private List<Comment> executeList(String url, String label, Long id) {
        ResponseEntity<Comment[]> response = restTemplate.getForEntity(url, Comment[].class, id);
        log.info("Third-party {} status={} count={}", label, response.getStatusCodeValue(),
                response.getBody() == null ? 0 : response.getBody().length);
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Data
    public static class Post {
        private Long userId;
        private Long id;
        private String title;
        private String body;
    }

    @Data
    public static class Comment {
        private Long postId;
        private Long id;
        private String name;
        private String email;
        private String body;
    }
}
