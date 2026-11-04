package com.nulvora.nulvora.productVariant.controller;

import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-variant")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping("/{productId}/new/variants")
    public ResponseEntity<ProductVariantResponse> createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        ProductVariantResponse response =
                productVariantService.createVariant(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
