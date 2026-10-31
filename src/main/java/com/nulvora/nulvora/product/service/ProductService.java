package com.nulvora.nulvora.product.service;

import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.response.ProductPostResponse;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.product.mapper.ProductMapper;
import com.nulvora.nulvora.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final GlobalHelperService globalHelperService;



    // criar produtos
    ProductPostResponse createProduct(ProductRequest request){
        // busca a categoria
        Category category = globalHelperService.getCategoryById(request.categoryId());

        // cria o produto
        Product product = productMapper.toEntity(request);

        // relaciona com categoria
        product.setCategory(category);

        return productMapper.toProductPostResponse(productRepository.save(product));

    }

}
