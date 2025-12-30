package com.hipradeep.code.controller;

import com.hipradeep.code.service.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StreamingController {

    @Autowired
    private StreamingService service;

    @GetMapping(value = "/stream/{title}", produces = "video/mp4")
    public Mono<Resource> streamVideo(@PathVariable String title,
                                         @RequestHeader(value = "Range", required = false) String range) {
        System.out.println("Range in bytes() : " + range);
        return service.getVideoMp4(title);
    }

    @GetMapping(value = "/stream/mkv/{title}", produces = "video/x-matroska")
    public Mono<Resource> streamVideoMkv(@PathVariable String title,
            @RequestHeader(value = "Range", required = false) String range) {
        System.out.println("Range in bytes() : " + range);
        return service.getVideoMkv(title);
    }



    @GetMapping(value = "/stream/ftp/{title}", produces = "video/mp4")
    public Mono<Resource> streamVideoFtp(@PathVariable String title) {
        return service.getVideoFromFtp(title);
    }

    @GetMapping(value = "/stream/db/{title}", produces = "video/mp4")
    public Mono<Resource> streamVideoDb(@PathVariable String title) {
        return service.getVideoFromDatabase(title);
    }
}
