package com.nulvora.nulvora.product.service;

import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.dto.request.ProductAttributeRequest;
import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.response.ProductPostResponse;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.product.mapper.ProductMapper;
import com.nulvora.nulvora.product.repository.ProductRepository;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttribute.service.ProductAttributeService;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final GlobalHelperService globalHelperService;
    private final ProductAttributeService productAttributeService;
    private final ProductAttributeOptionService productAttributeOptionService;


    // criar produtos
    @Transactional
    ProductPostResponse createProduct(ProductRequest request){
        // busca a categoria
        Category category = globalHelperService.getCategoryById(request.categoryId());

        // cria o produto
        Product product = productMapper.toEntity(request);

        // relaciona com categoria
        product.setCategory(category);

        // percorrer os atributos e criar eles
        for (ProductAttributeRequest attribute : request.attributes()){
            ProductAttribute productAttribute = productAttributeService
                    .createProductAttribute(product, attribute.name());

            // cria as opções para cada atributo
            for (String option : attribute.options()){
                productAttributeOptionService.createAttributeOption(productAttribute, option);
            }
        }


        return productMapper.toProductPostResponse(productRepository.save(product));

    }

}
