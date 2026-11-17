package com.example.clientmanagement.service;

import com.example.clientmanagement.config.ThirdPartyProperties;
import com.example.clientmanagement.dto.ClientResponse;
import com.example.clientmanagement.dto.GeoResponse;
import com.example.clientmanagement.integration.ThirdPartyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClientProbeService {

    private static final Logger log = LoggerFactory.getLogger(ClientProbeService.class);

    private final ClientService clientService;
    private final ThirdPartyClient thirdPartyClient;
    private final ThirdPartyProperties properties;

    public ClientProbeService(ClientService clientService, ThirdPartyClient thirdPartyClient,
                              ThirdPartyProperties properties) {
        this.clientService = clientService;
        this.thirdPartyClient = thirdPartyClient;
        this.properties = properties;
    }

    public GeoResponse probe(Long id) {
        ClientResponse client = clientService.getById(id);
        long userOffset = properties.getUserOffset();
        long userId = userOffset > 0 ? ((id - 1) % userOffset) + 1 : id;
        log.info("Nested call clientId={} thirdPartyUserId={}", client.id(), userId);
        return thirdPartyClient.fetchProfile(
                String.valueOf(client.id()), client.name(), client.email(), userId);
    }
}
