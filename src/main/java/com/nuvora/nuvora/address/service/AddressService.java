package com.nuvora.nuvora.address.service;

import com.nuvora.nuvora.address.dto.request.AddressRequest;
import com.nuvora.nuvora.address.dto.request.PutAddressRequest;
import com.nuvora.nuvora.address.dto.response.AddressResponse;
import com.nuvora.nuvora.address.entity.Address;
import com.nuvora.nuvora.address.mapper.AddressMapper;
import com.nuvora.nuvora.address.repository.AddressRepository;
import com.nuvora.nuvora.helper.GlobalHelperService;
import com.nuvora.nuvora.user.entity.User;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final GlobalHelperService globalHelperService;


    // get de todos os endereços do usuario logado
    public Page<AddressResponse> getMyAddress(Pageable pageable){
        return addressRepository.findByUserId(
                globalHelperService.getLoggedUser().getId(), pageable
        ).map(addressMapper::toAddressResponse);

    }

    // get de um endereço do usuario pelo id do endereço
    public AddressResponse getMyAddressById(Long addressId){
        return addressMapper.toAddressResponse(findAddressById(addressId));
    }

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

    // editar o endereço pelo id
    @Transactional
    public AddressResponse updateAddress(PutAddressRequest request, Long addressId){
        Address address = findAddressById(addressId);

        // Atualiza somente os campos enviados
        addressMapper.updateEntity(request, address);

        // Normaliza os dados
        address.setZipCode(normalizeZipCode(address.getZipCode()));

        address.setComplement(
                address.getComplement() == null
                        ? ""
                        : address.getComplement().trim()
        );

        // Verifica duplicidade, ignorando o próprio endereço
        if (addressRepository.existsByUserIdAndZipCodeAndStreetAndNumberAndComplementAndIdNot(
                address.getUser().getId(),
                address.getZipCode(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um endereço com esses dados"
            );
        }

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }


    // normalizar o cep
    private String normalizeZipCode(String zipCode) {
        return zipCode.replaceAll("\\D", "");
    }


    private Address findAddressById(Long addressId){
        Long userId = globalHelperService.getLoggedUser().getId();
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Endereço não encontrado"
                ));

    }
}
