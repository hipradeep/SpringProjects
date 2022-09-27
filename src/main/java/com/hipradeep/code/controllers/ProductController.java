package com.hipradeep.code.controllers;


import com.hipradeep.code.payloads.ApiResponse;
import com.hipradeep.code.payloads.ProductDto;
import com.hipradeep.code.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto productDto) throws Exception {


        ProductDto product= null;
        try {
            product = this.productService.createProduct(productDto);
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse<ProductDto>>(new ApiResponse<>(e.getMessage(), false), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse<>("Product created successfully!", true, product), HttpStatus.CREATED);

    }
}
