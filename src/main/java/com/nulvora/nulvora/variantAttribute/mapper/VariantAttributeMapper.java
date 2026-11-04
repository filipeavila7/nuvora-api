package com.nulvora.nulvora.variantAttribute.mapper;

import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantAttributeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariant", source = "productVariant")
    @Mapping(target = "option", source = "option")
    VariantAttribute toEntity(
            ProductVariant productVariant,
            ProductAttributeOption option
    );
}