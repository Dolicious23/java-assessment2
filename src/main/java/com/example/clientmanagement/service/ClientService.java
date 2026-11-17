package com.example.clientmanagement.service;

import com.example.clientmanagement.domain.Client;
import com.example.clientmanagement.dto.ClientRequest;
import com.example.clientmanagement.dto.ClientResponse;
import com.example.clientmanagement.dto.PageResponse;
import com.example.clientmanagement.exception.ClientNotFoundException;
import com.example.clientmanagement.exception.DuplicateEmailException;
import com.example.clientmanagement.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClientResponse create(ClientRequest request) {
        String email = normalize(request.email());
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException(email);
        }
        LocalDateTime now = LocalDateTime.now();
        Client client = Client.builder()
                .name(request.name().trim())
                .email(email)
                .phone(request.phone())
                .company(request.company())
                .city(request.city())
                .createdAt(now)
                .updatedAt(now)
                .build();
        Client saved = repository.save(client);
        log.info("Client created id={} email={}", saved.getId(), saved.getEmail());
        return ClientResponse.from(saved);
    }

    @Transactional
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        String email = normalize(request.email());
        repository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEmailException(email);
                });

        client.setName(request.name().trim());
        client.setEmail(email);
        client.setPhone(request.phone());
        client.setCompany(request.company());
        client.setCity(request.city());
        client.setUpdatedAt(LocalDateTime.now());

        Client saved = repository.save(client);
        log.info("Client updated id={} email={}", saved.getId(), saved.getEmail());
        return ClientResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public ClientResponse getById(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return ClientResponse.from(client);
    }

    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> search(String keyword, int page) {
        PageRequest pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Client> result = (keyword == null || keyword.isBlank())
                ? repository.findAll(pageable)
                : repository.search(keyword.trim(), pageable);

        Page<ClientResponse> mapped = result.map(ClientResponse::from);
        return new PageResponse<>(
                mapped.getContent(),
                mapped.getNumber(),
                mapped.getSize(),
                mapped.getTotalElements(),
                mapped.getTotalPages(),
                mapped.isFirst(),
                mapped.isLast());
    }

    private String normalize(String email) {
        return email.trim().toLowerCase();
    }
}
