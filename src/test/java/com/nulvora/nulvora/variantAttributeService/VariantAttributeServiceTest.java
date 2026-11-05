package com.nulvora.nulvora.variantAttributeService;


import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.service.ProductVariantService;
import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
import com.nulvora.nulvora.variantAttribute.repository.VariantAttributeRepository;
import com.nulvora.nulvora.variantAttribute.service.VariantAttributeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VariantAttributeServiceTest {

    @Mock
    private VariantAttributeRepository variantAttributeRepository;

    @InjectMocks
    private VariantAttributeService variantAttributeService;


    @Test
    @DisplayName("Deve retornar exceção de conflito quando a combinação de atributos ja existir")
    void shouldThrowConflictWhenVariantCombinationAlreadyExists(){
        Long productId = 1L; // cria o id do produto

        Set<Long> optionsIds = Set.of(10L, 20L); // opções que vão no request

        // variante para usar na lista de variantes cadastradas
        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(1L);

        // options para relacionar com a variant
        ProductAttributeOption productAttributeOption1 = new ProductAttributeOption();
        productAttributeOption1.setId(10L);

        ProductAttributeOption productAttributeOption2 = new ProductAttributeOption();
        productAttributeOption2.setId(20L);

        // variatAttributes onde relaciona uma option com uma variant
        VariantAttribute variantAttribute1 = new VariantAttribute();
        variantAttribute1.setOption(productAttributeOption1);
        variantAttribute1.setProductVariant(productVariant);


        VariantAttribute variantAttribute2 = new VariantAttribute();
        variantAttribute2.setOption(productAttributeOption2);
        variantAttribute2.setProductVariant(productVariant);


        // lista de variantAttribute que o metodo da repository retorna
        List<VariantAttribute> variantAttributes = List.of(
              variantAttribute1, variantAttribute2
        );

        // mock do metodo
        when(variantAttributeRepository
                .findAllByOption_ProductAttribute_Product_Id(productId))
                .thenReturn(variantAttributes);

        // deve retornar essa exceção quando usar a service que esta sendo testada
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> variantAttributeService.validateVariantCombination(optionsIds, productId)
        );

        // verifica se o resultado é o experado
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }
}
