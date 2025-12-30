package com.hipradeep.code.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorMap = new HashMap<>();
        Throwable error = getError(request);
        errorMap.put("message", error.getMessage());
        errorMap.put("endpoint url", request.path());

        // Handle ProductNotFoundException specifically if needed, or generally for all
        if (error instanceof ProductNotFoundException) {
            errorMap.put("status", HttpStatus.NOT_FOUND.value());
            errorMap.put("error", "Product Not Found");
        } else {
            // Fallback or let DefaultErrorAttributes logic handle others if we called super
            // But here we are building a fresh map.
            // Let's mimic standard behavior or defaults
            errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorMap.put("error", "Internal Server Error");
        }
        return errorMap;
    }
}
