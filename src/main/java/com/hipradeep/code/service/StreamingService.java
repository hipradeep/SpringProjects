package com.hipradeep.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamingService {

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String FORMAT = "classpath:videos/%s.mkv";
    private static final String FORMAT_MP4 = "classpath:videos/%s.mp4";

    public Mono<Resource> getVideoMkv(String title) {
        return Mono.fromSupplier(() -> resourceLoader.getResource(String.format(FORMAT, title)));
    }

    public Mono<Resource> getVideoMp4(String title) {
        return Mono.fromSupplier(() -> resourceLoader.getResource(String.format(FORMAT_MP4, title)));
    }

    // Example 1: Stream from FTP Server
    // Format: ftp://user:password@host:port/path/to/file
    public Mono<Resource> getVideoFromFtp(String title) {
        // REPLACE with actual FTP credentials
        String ftpUrl = String.format("ftp://user:pass@localhost/%s.mp4", title);
        return Mono.fromSupplier(() -> resourceLoader.getResource(ftpUrl));
    }

    // Example 2: Stream from Database (Simulated)
    public Mono<Resource> getVideoFromDatabase(String title) {
        // In a real app, you would use R2dbcRepository to fetch the BLOB/byte[]
        // return repository.findByName(title).map(video -> new
        // ByteArrayResource(video.getContent()));

        // Simulation: Returning a dummy byte array
        return Mono.fromSupplier(
                () -> new org.springframework.core.io.ByteArrayResource("Simulated Database Video Content".getBytes()));
    }
}
