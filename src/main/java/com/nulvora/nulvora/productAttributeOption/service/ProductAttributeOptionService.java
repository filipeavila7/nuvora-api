package com.nulvora.nulvora.productAttributeOption.service;


import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.mapper.ProductAttributeOptionMapper;
import com.nulvora.nulvora.productAttributeOption.repository.ProductAttributeOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductAttributeOptionService {
    private final ProductAttributeOptionRepository productAttributeOptionRepository;
    private final ProductAttributeOptionMapper productAttributeOptionMapper;


    public ProductAttributeOption createAttributeOption(ProductAttribute productAttribute, String value){
        return productAttributeOptionRepository
                .save(productAttributeOptionMapper.createProductAttributeOption(productAttribute, value));
    }


    // verificar se todos os atributos do produto estão sendo usados pela variante
    public void validadeAttributeQuantitiy(Set<Long> optionsIds, Product product) {

        // cria um set com o id dos atributos do produto
        Set<Long> attributeIds = product.getProductAttributes()
                .stream().map(ProductAttribute::getId)
                .collect(Collectors.toSet());

        // cria um set com os ids dos atributos pelo id das opçôes passadas
        Set<Long> receivedAttributeIds = productAttributeOptionRepository
                .findAllById(optionsIds)
                .stream()
                .map(option -> option.getProductAttribute().getId())
                .collect(Collectors.toSet());

        // verifica se são diferentes
        if (!attributeIds.equals(receivedAttributeIds)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Informe exatamente uma opção para cada atributo do produto"
            );

        }

    }


    public ProductAttributeOption getOptionOrThrow(Long optionId) {
        return productAttributeOptionRepository.findById(optionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Opção de atributo não encontrada"
                ));
    }
}
