package com.nulvora.nulvora.address.mapper;

import com.nulvora.nulvora.address.dto.request.AddressRequest;
import com.nulvora.nulvora.address.dto.request.PutAddressRequest;
import com.nulvora.nulvora.address.dto.response.AddressResponse;
import com.nulvora.nulvora.address.entity.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressRequest request);

    AddressResponse toAddressResponse(Address address);


    // atualizar os campos de Addres ignorando o id e o user e valores nulos passados
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(PutAddressRequest request, @MappingTarget Address address);
}
