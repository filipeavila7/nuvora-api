package com.nulvora.nulvora.productAttributeOption.mapper;



import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAttributeOptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productAttribute", source = "productAttribute")
    @Mapping(target = "value", source = "value")
    ProductAttributeOption createProductAttributeOption(ProductAttribute productAttribute, String value);


}
