package com.nulvora.nulvora.product.controller;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.response.ProductResponse;

import com.nulvora.nulvora.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/new")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }
}
