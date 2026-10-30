package com.nuvora.nuvora.address.mapper;

import com.nuvora.nuvora.address.dto.request.AddressRequest;
import com.nuvora.nuvora.address.dto.response.AddressResponse;
import com.nuvora.nuvora.address.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressRequest request);

    AddressResponse toAddressResponse(Address address);
}
