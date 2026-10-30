package com.nuvora.nuvora.address.service;

import com.nuvora.nuvora.address.dto.request.AddressRequest;
import com.nuvora.nuvora.address.dto.response.AddressResponse;
import com.nuvora.nuvora.address.entity.Address;
import com.nuvora.nuvora.address.mapper.AddressMapper;
import com.nuvora.nuvora.address.repository.AddressRepository;
import com.nuvora.nuvora.helper.GlobalHelperService;
import com.nuvora.nuvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final GlobalHelperService globalHelperService;

    // criar endereço
    public AddressResponse createAdress(AddressRequest request){
        User loggedUser = globalHelperService.getLoggedUser();

        // normalizar o cep
        String zipCode = normalizeZipCode(request.zipCode());

        // verifica se o usuario esta cadastrando um endereço duplicado
        if (addressRepository.existsByUserIdAndZipCodeAndStreetAndNumberAndComplement(
                loggedUser.getId(), zipCode, request.street(),
                request.number(), request.complement()
        )){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Endereço ja cadastrado"
            );
        }

        // cria o objeto
        Address address = addressMapper.toEntity(request);

        // se o complemento for null, coloca uma string vazia
        address.setComplement(
                request.complement() == null
                        ? ""
                        : request.complement()
        );

        // coloca o cep normalizado
        address.setZipCode(zipCode);

        // relaciona com o user logado
        address.setUser(loggedUser);

        return addressMapper.toAddressResponse(addressRepository.save(address));

    }


    private String normalizeZipCode(String zipCode) {
        return zipCode.replaceAll("\\D", "");
    }
}
