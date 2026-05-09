package com.test.demo.controller;

import com.test.demo.model.Comment;
import com.test.demo.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.findAllSync();
    }

    @GetMapping("/{id}")
    public Mono<Comment> getCommentById(@PathVariable Integer id) {
        return commentService.findByIdAsync(id);
    }
}
