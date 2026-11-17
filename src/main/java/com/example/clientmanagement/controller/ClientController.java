package com.example.clientmanagement.controller;

import com.example.clientmanagement.dto.ClientRequest;
import com.example.clientmanagement.dto.ClientResponse;
import com.example.clientmanagement.dto.GeoResponse;
import com.example.clientmanagement.dto.PageResponse;
import com.example.clientmanagement.logging.AuditLogger;
import com.example.clientmanagement.service.ClientProbeService;
import com.example.clientmanagement.service.ClientService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

@RestController
@RequestMapping(path = "/api/v1/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ClientController {

    private final ClientService clientService;
    private final ClientProbeService probeService;
    private final AuditLogger auditLogger;

    public ClientController(ClientService clientService, ClientProbeService probeService, AuditLogger auditLogger) {
        this.clientService = clientService;
        this.probeService = probeService;
        this.auditLogger = auditLogger;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        auditLogger.record("CREATE_CLIENT", request);
        ClientResponse created = clientService.create(request);
        auditLogger.record("CREATE_CLIENT_DONE", created);
        return ResponseEntity
                .created(URI.create("/api/v1/clients/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        auditLogger.record("UPDATE_CLIENT id=" + id, request);
        ClientResponse updated = clientService.update(id, request);
        auditLogger.record("UPDATE_CLIENT_DONE id=" + id, updated);
        return updated;
    }

    @GetMapping("/{id}")
    public ClientResponse getById(@PathVariable Long id) {
        ClientResponse response = clientService.getById(id);
        auditLogger.record("GET_CLIENT id=" + id, response);
        return response;
    }

    @GetMapping
    public PageResponse<ClientResponse> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") @Min(0) int page) {
        PageResponse<ClientResponse> response = clientService.search(keyword, page);
        auditLogger.record("LIST_CLIENTS page=" + page, response);
        return response;
    }

    @GetMapping("/{id}/insight")
    public GeoResponse insight(@PathVariable Long id) {
        GeoResponse response = probeService.probe(id);
        auditLogger.record("CLIENT_INSIGHT id=" + id, response);
        return response;
    }
}
