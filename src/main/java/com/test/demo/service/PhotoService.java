package com.test.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class PhotoService {

    private static final Logger log = LoggerFactory.getLogger(PhotoService.class);
    private final ObjectMapper objectMapper;

    public PhotoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Photo findById(Integer id) {
        log.info("Fetching photo with id {} using HttpURLConnection (Java 8 way)", id);
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/photos/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Source", "Java-HttpURLConnection");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return objectMapper.readValue(response.toString(), Photo.class);
            } else {
                log.error("Failed to fetch photo. Response Code: {}", responseCode);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching photo with id {}: {}", id, e.getMessage());
            throw new RuntimeException("HttpURLConnection error", e);
        }
    }
}
