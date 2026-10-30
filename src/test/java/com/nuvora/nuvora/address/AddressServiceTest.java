package com.nuvora.nuvora.address;

import com.nuvora.nuvora.address.dto.request.PutAddressRequest;
import com.nuvora.nuvora.address.entity.Address;
import com.nuvora.nuvora.address.mapper.AddressMapper;
import com.nuvora.nuvora.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AddressServiceTest {

    private final AddressMapper mapper =
            Mappers.getMapper(AddressMapper.class);


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
}
