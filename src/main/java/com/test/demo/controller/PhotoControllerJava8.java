package com.test.demo.controller;

import com.test.demo.model.Photo;
import com.test.demo.service.PhotoServiceJava8;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos")
public class PhotoControllerJava8 {

    private final PhotoServiceJava8 photoService;

    public PhotoControllerJava8(PhotoServiceJava8 photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{id}")
    public Photo getPhotoById(@PathVariable Integer id) {
        return photoService.findById(id);
    }
}
