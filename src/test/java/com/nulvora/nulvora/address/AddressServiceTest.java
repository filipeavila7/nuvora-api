package com.nulvora.nulvora.address;

import com.nulvora.nulvora.address.dto.request.AddressRequest;
import com.nulvora.nulvora.address.dto.request.PutAddressRequest;
import com.nulvora.nulvora.address.entity.Address;
import com.nulvora.nulvora.address.mapper.AddressMapper;
import com.nulvora.nulvora.address.repository.AddressRepository;
import com.nulvora.nulvora.address.service.AddressService;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    private final AddressMapper mapper =
            Mappers.getMapper(AddressMapper.class);

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private GlobalHelperService globalHelperService;

    @InjectMocks
    private AddressService addressService;


    @Test
    @DisplayName("deve permitir apenas valores não nulos no put")
    void shouldUpdateOnlyAllowedFields(){
        // preparar os dados (arrange)
        User user = new User();
        user.setId(10L);

        Address address = new Address();
        address.setId(10L);
        address.setUser(user);
        address.setStreet("Rua antiga");
        address.setCity("Brasília");
        address.setZipCode("1234567");
        address.setComplement("Apartamento 202");


        PutAddressRequest request = new PutAddressRequest(
                "Rua nova",
                null,
                null,
                "São Paulo",
                null,
                null,
                ""
        );

        // faz uma ação (act)
        mapper.updateEntity(request, address);


        // verifica se os resultados batem (assert)

        // verifica se o id mudou e se o user mudou, na regra os 2 nâo podem mudar
        assertEquals(10L, address.getId());
        assertSame(user, address.getUser()); // assertSame verifica se os obejtos apontam para os mesmos valores na memoria


        // verifica se ocorreu a mudança dos valores passados
        assertEquals("Rua nova", address.getStreet());
        assertEquals("São Paulo", address.getCity());
        assertEquals("", address.getComplement());

        // verifica se ocorreu mudança em valores que não foram passados
        assertEquals("1234567", address.getZipCode());

    }


    @Test
    @DisplayName("deve lançar exceção caso o usuario tente criar um endereço duplicado")
    void shouldThrowExceptionWhenUserAlreadyHasAddress(){

        // criação dos dados
        AddressRequest request = new AddressRequest("rua shipo", "12", "bairro shipo",
                "cidade shipo", "SP", "1239574", "shipo shipo");


        User loggedUser = new User();
        loggedUser.setId(1L);


        when(globalHelperService.getLoggedUser())
                .thenReturn(loggedUser);

        // o metodo deve retornar true, ou seja existe repetido
        when(addressRepository.existsByUserIdAndZipCodeAndStreetAndNumberAndComplement(1L, "1239574",
                "rua shipo", "12", "shipo shipo")).thenReturn(true);



        // testar se a service vai lançar a exceção esperada
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> addressService.createAdress(request));


        // testa pra ver se retornou o 409
        assertEquals(409, exception.getStatusCode().value());


        // verifica tambem se o metodo de save() nunca foi chamado
        verify(addressRepository, never()).save(any(Address.class));

    }
}
