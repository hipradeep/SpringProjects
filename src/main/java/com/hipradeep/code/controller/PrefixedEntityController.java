package com.hipradeep.code.controller;

import com.hipradeep.code.model.PrefixedEntity;
import com.hipradeep.code.repository.PrefixedEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing endpoints for creating and retrieving PrefixedEntities.
 */
@RestController
@RequestMapping("/api/prefixed-entities")
public class PrefixedEntityController {

    private final PrefixedEntityRepository prefixedEntityRepository;

    @Autowired
    public PrefixedEntityController(PrefixedEntityRepository prefixedEntityRepository) {
        this.prefixedEntityRepository = prefixedEntityRepository;
    }

    /**
     * Creates a new PrefixedEntity. The primary key ID is generated using custom PrefixedUUIDGenerator strategy.
     */
    @PostMapping
    public ResponseEntity<PrefixedEntity> createPrefixedEntity(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.getOrDefault("name", "Default Prefixed Entity");

        PrefixedEntity prefixedEntity = new PrefixedEntity(name);
        PrefixedEntity savedEntity = prefixedEntityRepository.save(prefixedEntity);
        return ResponseEntity.ok(savedEntity);
    }

    /**
     * Retrieves a single PrefixedEntity by its custom Prefixed UUID ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrefixedEntity> getPrefixedEntityById(@PathVariable String id) {
        return prefixedEntityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists all PrefixedEntities currently registered in the database.
     */
    @GetMapping
    public ResponseEntity<List<PrefixedEntity>> getAllPrefixedEntities() {
        return ResponseEntity.ok(prefixedEntityRepository.findAll());
    }
}
