package com.hipradeep.code.controller;

import com.hipradeep.code.exception.InvalidRequestException;
import com.hipradeep.code.exception.ResourceNotFoundException;
import com.hipradeep.code.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final Map<Long, Item> itemRepository = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ItemController() {
        // Initial dummy data without DB
        itemRepository.put(1L, new Item(1L, "Laptop", 1200.00));
        itemRepository.put(2L, new Item(2L, "Smartphone", 800.00));
        idGenerator.set(3);
    }

    @GetMapping
    public Collection<Item> getAllItems() {
        return itemRepository.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemRepository.get(id);
        if (item == null) {
            throw new ResourceNotFoundException("Item with ID " + id + " not found");
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item newItem) {
        if (newItem.getName() == null || newItem.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Item name cannot be empty");
        }
        if (newItem.getPrice() == null || newItem.getPrice() <= 0) {
            throw new InvalidRequestException("Item price must be greater than zero");
        }
        long id = idGenerator.getAndIncrement();
        newItem.setId(id);
        itemRepository.put(id, newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @GetMapping("/trigger-500")
    public void triggerInternalError() {
        throw new RuntimeException("Simulated internal error for testing HTTP 500 handling");
    }
}
