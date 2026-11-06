package com.nulvora.nulvora.product.service;

import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.category.service.CategoryService;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.dto.request.ProductAttributeRequest;
import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.request.ProductUpdateRequest;
import com.nulvora.nulvora.product.dto.response.ProductResponse;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.product.mapper.ProductMapper;
import com.nulvora.nulvora.product.repository.ProductRepository;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttribute.service.ProductAttributeService;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import com.nulvora.nulvora.productVariant.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductAttributeService productAttributeService;
    private final ProductAttributeOptionService productAttributeOptionService;
    private final ProductVariantService productVariantService;
    private final GlobalHelperService globalHelperService;


    // criar produtos
    @Transactional
    public ProductResponse createProduct(ProductRequest request){
        // busca a categoria
        Category category = categoryService.getCategoryByIdOrThrow(request.categoryId());

        // cria o produto
        Product product = productMapper.toEntity(request);

        // relaciona com categoria
        product.setCategory(category);

        // cria os atributos e as opções para eles
        createAttributesAndOptions(product, request.attributes());

        // salva o produto
        Product productSave = productRepository.save(product);

        // cria uma variante padrão para o produto criado
        productVariantService.createStandardVariant(product);

        return productMapper.toProductResponse(productSave);

    }


    // editar produtos
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request){
        Product product = globalHelperService.getActiveProductOrThrow(productId);

        // atualiza somente os campos que não são nulos
        productMapper.updateEntity(request, product);

        // edita a categoria
        if (request.categoryId() != null){
            Category category = categoryService.getCategoryByIdOrThrow(request.categoryId());
            product.setCategory(category);
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }


    // criar atributos e opções para o produto
    private void createAttributesAndOptions(Product product, List<ProductAttributeRequest> request){
        for (ProductAttributeRequest attribute : request){
            ProductAttribute productAttribute = productAttributeService
                    .createProductAttribute(product, attribute.name());

            // garante o relacionamento dos 2 lados
            product.addProductAttribute(productAttribute);

            // cria as opções para cada atributo
            for (String option : attribute.options()){
                ProductAttributeOption attributeOption = productAttributeOptionService
                        .createAttributeOption(productAttribute, option);

                // garante o relacionamento dos 2 lados
                productAttribute.addOption(attributeOption);
            }
        }
    }




}
