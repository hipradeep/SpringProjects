package com.test.demo.service;

import com.test.demo.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TodoService {

    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final RestTemplate restTemplate;

    public TodoService() {
        // Step 1: Initialize the legacy RestTemplate.
        this.restTemplate = new RestTemplate();
    }

    public Todo findById(Integer id) {
        log.info("Fetching todo with id {} using RestTemplate (Legacy)", id);
        
        // Step 2: Define the target URL.
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;

        // Step 3: Create HttpHeaders and set any required custom headers.
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source", "Spring-RestTemplate");
        
        // Step 4: Wrap the headers (and optionally a body) in an HttpEntity object.
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Step 5: Execute the request using the exchange method.
        // This method allows us to pass the HttpEntity and specify the expected response type.
        ResponseEntity<Todo> response = restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class);
        
        // Step 6: Extract the deserialized body from the ResponseEntity.
        return response.getBody();
    }
    public Todo createTodo(Todo newTodo) {
        log.info("Creating a new todo using RestTemplate (Legacy POST)");

        // Step 1: Define the target URL for creating a resource.
        String url = "https://jsonplaceholder.typicode.com/todos";

        // Step 2: Create HttpHeaders and set custom headers, e.g., Content-Type.
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source", "Spring-RestTemplate-POST");
        headers.set("Content-Type", "application/json");

        // Step 3: Wrap the headers and the body (newTodo object) in an HttpEntity.
        HttpEntity<Todo> entity = new HttpEntity<>(newTodo, headers);

        // Step 4: Execute the POST request using exchange (or postForObject).
        // Here we use postForObject which is a convenient method for POST requests.
        Todo createdTodo = restTemplate.postForObject(url, entity, Todo.class);

        return createdTodo;
    }
}
