package com.nulvora.nulvora.productAttributeOption;


import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.repository.ProductAttributeOptionRepository;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductAttributeOptionServiceTest {

    @Mock
    private ProductAttributeOptionRepository productAttributeOptionRepository;

    @InjectMocks
    private ProductAttributeOptionService productAttributeOptionService;



    @Test
    @DisplayName("Não deve lançar exceção quando receber uma opção para cada atributo")
    void shouldNotThrowWhenReceivedOptionsMatchProductAttributes() {

        Product product = new Product(); // cria o produto

        // cria 2 atributos para ele
        ProductAttribute attribute1 = new ProductAttribute();
        attribute1.setId(1L);

        ProductAttribute attribute2 = new ProductAttribute();
        attribute2.setId(2L);

        // relaciona
        product.setProductAttributes(List.of(attribute1, attribute2));

        // cria o request
        Set<Long> optionsIds = Set.of(10L, 20L);

        // cria as opções que se relacionam com os atributos
        ProductAttributeOption option1 = new ProductAttributeOption();
        option1.setId(10L);
        option1.setProductAttribute(attribute1);

        ProductAttributeOption option2 = new ProductAttributeOption();
        option2.setId(20L);
        option2.setProductAttribute(attribute2);


        // o findAll deve retornar uma lista com as opções que se relacionam com os atributos do produto
        when(productAttributeOptionRepository.findAllById(optionsIds))
                .thenReturn(List.of(option1, option2));


        // verifica se não lançou exceção
        assertDoesNotThrow(() ->
                productAttributeOptionService.validadeAttributeQuantitiy(optionsIds, product)
        );
    }


    @Test
    @DisplayName("Deve lançar conflito quando faltar opção de algum atributo")
    void shouldThrowConflictWhenMissingAttributeOption(){
        Product product = new Product(); // cria o produto

        // cria 2 atributos para ele
        ProductAttribute attribute1 = new ProductAttribute();
        attribute1.setId(1L);

        ProductAttribute attribute2 = new ProductAttribute();
        attribute2.setId(2L);

        // relaciona
        product.setProductAttributes(List.of(attribute1, attribute2));

        // cria o request
        Set<Long> optionsIds = Set.of(10L);

        // cria as opções que se relacionam com os atributos
        ProductAttributeOption option1 = new ProductAttributeOption();
        option1.setId(10L);
        option1.setProductAttribute(attribute1);


        // o findAll deve retornar uma lista com as opções que se relacionam com os atributos do produto
        when(productAttributeOptionRepository.findAllById(optionsIds))
                .thenReturn(List.of(option1));

        assertThrows(ResponseStatusException.class, () ->
                productAttributeOptionService.validadeAttributeQuantitiy(optionsIds, product)
        );

    }


    @Test
    @DisplayName("Deve lançar conflito quando receber opções de atributos repetidos")
    void shouldThrowConflictWhenReceivedOptionsHaveRepeatedAttributes() {

        Product product = new Product();

        ProductAttribute color = new ProductAttribute();
        color.setId(1L);

        ProductAttribute size = new ProductAttribute();
        size.setId(2L);

        product.setProductAttributes(List.of(color, size));

        Set<Long> optionsIds = Set.of(10L, 11L);

        ProductAttributeOption option1 = new ProductAttributeOption();
        option1.setId(10L);
        option1.setProductAttribute(color);

        ProductAttributeOption option2 = new ProductAttributeOption();
        option2.setId(11L);
        option2.setProductAttribute(color);

        when(productAttributeOptionRepository.findAllById(optionsIds))
                .thenReturn(List.of(option1, option2));

        assertThrows(ResponseStatusException.class, () ->
                productAttributeOptionService.validadeAttributeQuantitiy(optionsIds, product)
        );
    }
}
