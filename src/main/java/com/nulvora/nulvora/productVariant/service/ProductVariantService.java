package com.nulvora.nulvora.productVariant.service;

import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.product.repository.ProductRepository;
import com.nulvora.nulvora.product.service.ProductService;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import com.nulvora.nulvora.productImg.service.ProductImgService;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.mapper.ProductVariantMapper;
import com.nulvora.nulvora.productVariant.repository.ProductVariantRepository;
import com.nulvora.nulvora.variantAttribute.service.VariantAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final VariantAttributeService variantAttributeService;
    private final ProductAttributeOptionService productAttributeOptionService;
    private final ProductImgService productImgService;
    private final GlobalHelperService globalHelperService;


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


    // criar variante de produtos
    @Transactional
    public ProductVariantResponse createVariant(Long productId, ProductVariantRequest request){
        Product product = globalHelperService.getActiveProductOrThrow(productId);

        // verificar se os ids das opções passadas representam uma combinação ja existente
        variantAttributeService.validateVariantCombination(request.optionsIds(), productId);

        // verifca se a variante colocou todos os atributos necessários do produto
        productAttributeOptionService.validadeAttributeQuantitiy(request.optionsIds(), product);

        // cria o objeto e relaciona como produto
        ProductVariant variant = productVariantMapper.toEntity(request);
        variant.setProduct(product);

        // salva a variante no banco
        ProductVariant variantSave = productVariantRepository.save(variant);

        // salva as imagens no banco
        productImgService.createVariantImgs(variantSave, request.images());

        // percorre as opções e salva no banco
        for (Long optionId : request.optionsIds()){
            variantAttributeService.createVariantAttribute(variant, optionId );

        }

        // salva
        return productVariantMapper.toResponse(variantSave);

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
