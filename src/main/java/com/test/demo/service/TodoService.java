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
        this.restTemplate = new RestTemplate();
    }

    public Todo findById(Integer id) {
        log.info("Fetching todo with id {} using RestTemplate (Legacy)", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;

        // Example of adding headers in RestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source", "Spring-RestTemplate");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Todo> response = restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class);
        
        return response.getBody();
    }
}
