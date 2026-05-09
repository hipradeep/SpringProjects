package com.test.demo.controller;

import com.test.demo.model.Todo;
import com.test.demo.service.TodoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Integer id) {
        return todoService.findById(id);
    }

    @org.springframework.web.bind.annotation.PostMapping
    public Todo createTodo(@org.springframework.web.bind.annotation.RequestBody Todo newTodo) {
        return todoService.createTodo(newTodo);
    }
}
