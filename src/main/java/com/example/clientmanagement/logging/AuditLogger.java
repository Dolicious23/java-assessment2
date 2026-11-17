package com.example.clientmanagement.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    private static final Logger log = LoggerFactory.getLogger("AUDIT-LOGGER");
    private final ObjectMapper objectMapper;

    public AuditLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void record(String action, Object payload) {
        try {
            log.info("action={} payload={}", action, objectMapper.writeValueAsString(payload));
        } catch (Exception ex) {
            log.warn("action={} payload=<serialization failed: {}>", action, ex.getMessage());
        }
    }
}
