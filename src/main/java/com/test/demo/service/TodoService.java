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
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

@Service
public class TodoService {

    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final RestTemplate restTemplate;

    public TodoService() {
        // Initialize the legacy RestTemplate
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }

//    public TodoService(RestTemplateBuilder builder) {
//        this.restTemplate = builder
//                .rootUri("https://jsonplaceholder.typicode.com")
//                .build();
//    }

    public Todo findById(Integer id) {
        log.info("Fetching todo with id {} using RestTemplate (Legacy)", id);
        
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;

        // Set custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source", "Spring-RestTemplate");
        
        // Wrap the headers in an HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Execute the request and map response to Todo.class
        ResponseEntity<Todo> response = restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class);
        
        return response.getBody();
    }
    public Todo createTodo(Todo newTodo) {
        log.info("Creating a new todo using RestTemplate (Legacy POST)");

        String url = "https://jsonplaceholder.typicode.com/todos";

        // Set custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Source", "Spring-RestTemplate-POST");
        headers.set("Content-Type", "application/json");

        // Wrap headers and body in an HttpEntity
        HttpEntity<Todo> entity = new HttpEntity<>(newTodo, headers);

        // Execute the POST request
        Todo createdTodo = restTemplate.postForObject(url, entity, Todo.class);

        return createdTodo;
    }

    public Todo getTodoUsingGetForObject(Integer id) {
        log.info("Fetching todo using getForObject for id {}", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        return restTemplate.getForObject(url, Todo.class);
    }

    public ResponseEntity<Todo> getTodoUsingGetForEntity(Integer id) {
        log.info("Fetching todo using getForEntity for id {}", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        return restTemplate.getForEntity(url, Todo.class);
    }

    public Todo createTodoUsingPostForObject(Todo newTodo) {
        log.info("Creating todo using postForObject");
        String url = "https://jsonplaceholder.typicode.com/todos";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Custom-Header", "Spring-RestTemplate-Exchange-Put");
        HttpEntity<Todo> entity = new HttpEntity<>(newTodo, headers);

       // return restTemplate.postForObject(url, entity, Todo.class);
        return restTemplate.postForObject(url, newTodo, Todo.class);
    }

    public ResponseEntity<Todo> createTodoUsingPostForEntity(Todo newTodo) {
        log.info("Creating todo using postForEntity");
        String url = "https://jsonplaceholder.typicode.com/todos";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Custom-Header", "Spring-RestTemplate-Exchange-Put");
        HttpEntity<Todo> entity = new HttpEntity<>(newTodo, headers);

         //return restTemplate.postForEntity(url, entity, Todo.class);
        return restTemplate.postForEntity(url, newTodo, Todo.class);
    }

    public void updateTodoUsingPut(Integer id, Todo updatedTodo) {
        log.info("Updating todo using put for id {}", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        restTemplate.put(url, updatedTodo);
    }

    public void deleteTodoUsingDelete(Integer id) {
        log.info("Deleting todo using delete for id {}", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        restTemplate.delete(url);
    }

    public ResponseEntity<Todo> executeExchange(Integer id) {
        log.info("Executing exchange for full control for id {}", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Custom-Header", "Spring-RestTemplate-Exchange");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class);
    }

    public ResponseEntity<Todo> updateTodoUsingExchange(Integer id, Todo updatedTodo) {
        log.info("Updating todo using exchange for id {}", id);
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Custom-Header", "Spring-RestTemplate-Exchange-Put");
        HttpEntity<Todo> entity = new HttpEntity<>(updatedTodo, headers);
        
        return restTemplate.exchange(url, HttpMethod.PUT, entity, Todo.class);
    }

    private static class CustomResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().isError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
            // Add custom error handling logic here if needed
        }
    }
}
