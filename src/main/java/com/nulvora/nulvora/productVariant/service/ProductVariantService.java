package com.nulvora.nulvora.productVariant.service;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.product.repository.ProductRepository;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.mapper.ProductVariantMapper;
import com.nulvora.nulvora.productVariant.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper productVariantMapper;

    // criar variante padrão automaticamente
    public void createStandardVariant(Product product){
        ProductVariant defaultVariant = productVariantMapper.toDefaultVariant(product);

        // cria a variante padrão
        defaultVariant.setName("Padrão");
        defaultVariant.setPrice(BigDecimal.ZERO);
        defaultVariant.setStockQuantity(0);
        defaultVariant.setSku(generateUniqueSku());

        productVariantRepository.save(defaultVariant);
    }


    public ProductVariantResponse createVariant(Long productId, ProductVariantRequest request){
        Product product = getActiveProductOrThrow(productId);


    }



    public Product getActiveProductOrThrow(Long productId) {
        return productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"
                ));
    }


    // gerar sku aleatório
    private String generateSku() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    // caso o sku aleatório ja exista, cria outro
    public String generateUniqueSku() {
        String sku;

        do {
            sku = generateSku();
        } while (productVariantRepository.existsBySku(sku));

        return sku;
    }




}
