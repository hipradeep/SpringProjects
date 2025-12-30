package com.hipradeep.code.router;

import com.hipradeep.code.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(ProductHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/router/products")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getProducts)
                .andRoute(RequestPredicates.GET("/router/products/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getProductById)
                .andRoute(RequestPredicates.POST("/router/products/save")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::saveProduct)
                .andRoute(RequestPredicates.PUT("/router/products/update/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::updateProduct)
                .andRoute(RequestPredicates.DELETE("/router/products/delete/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteProduct)
                .andRoute(RequestPredicates.GET("/router/products/product-range")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getProductInRange);
    }
}
