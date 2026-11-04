package com.nulvora.nulvora.variantAttribute.service;

import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
import com.nulvora.nulvora.variantAttribute.mapper.VariantAttributeMapper;
import com.nulvora.nulvora.variantAttribute.repository.VariantAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VariantAttributeService {
    private final VariantAttributeRepository variantAttributeRepository;
    private final VariantAttributeMapper variantAttributeMapper;
    private final ProductAttributeOptionService productAttributeOptionService;


    // valida se a combinação de opção ja foi usada por outra variante
    public void validateVariantCombination(Set<Long> optionsIds, Long productId){

        // pega todas as combinações existentes usando o productId
        List<VariantAttribute> variantAttributes =
                variantAttributeRepository
                        .findAllByOption_ProductAttribute_Product_Id(productId);

        // cria um map com o id de variantAttributes como chave e um set do id de opções como valor
        /*{ resultado esperado:
        1L = Set.of(10L, 20L),
        2L = Set.of(11L, 21L),
        3L = Set.of(12L, 22L)}*/
        Map<Long, Set<Long>> combinations = variantAttributes.stream()
                .collect(Collectors.groupingBy(
                        va -> va.getProductVariant().getId(),
                        Collectors.mapping(
                                va -> va.getOption().getId(),
                                Collectors.toSet()
                        )
                ));

        // compara com as opções passadas no argumento
        boolean alreadyExists = combinations.values().
                stream().anyMatch(existing -> existing.equals(optionsIds));


        if (alreadyExists){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Essas opções ja foram utilizadas por outra variante");
        }
    }

    // criar atributo para variante
    public VariantAttribute createVariantAttribute(ProductVariant productVariant, Long optionId){

        // busca a option para ver se ela existe
        ProductAttributeOption option =
                productAttributeOptionService.getOptionOrThrow(optionId);

        // cria o objeto
        VariantAttribute variantAttribute = variantAttributeMapper.toEntity(productVariant, option);

        // faz o relacionamento do lado inverso
        productVariant.getVariantAttributes().add(variantAttribute);

        return variantAttributeRepository.save(variantAttribute);
    }



}
