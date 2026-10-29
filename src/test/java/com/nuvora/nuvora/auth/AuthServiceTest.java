package com.nuvora.nuvora.auth;


import com.nuvora.nuvora.auth.service.AuthService;
import com.nuvora.nuvora.jwt.JwtService;
import com.nuvora.nuvora.profile.service.ProfileService;
import com.nuvora.nuvora.refreshToken.service.RefreshTokenService;
import com.nuvora.nuvora.user.dto.request.UserRequest;
import com.nuvora.nuvora.user.entity.User;
import com.nuvora.nuvora.user.mapper.UserMapper;
import com.nuvora.nuvora.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;


    @Test // testar se o email ja existe está funcionando no metodo de register
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        // cria o objeto que vai na requisição
        UserRequest request = new UserRequest("shipin", "teste@email.com", "12345678");


        // quando a service chamar o metodo da repository, ele deve retornar true
        // when para simular uma dependencia, no caso o mock de UserRepository
        when(userRepository.existsByEmail("teste@email.com"))
                .thenReturn(true);

        // testar se lança a exceção de email ja existente
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, // qual exceção ele espera receber
                () -> authService.register(request) // a operação que deve lançar essa exceção
        );

        // verfica se a exceção retorna um 409
        assertEquals(409, exception.getStatusCode().value());


        // verificar se o usuario ainda não foi salvo
        // never(), nehuma chamada deve ter ocorrido
        // save(), verificar o metodo de save
        // any(User.class), pode ser qualquer objeto do tipo User
        // se esse metodo fosse chamado, o test não iria passar, pois colocamos o never
        verify(userRepository, never()).save(any(User.class)); // Se o e-mail já existe, o sistema deve interromper o cadastro antes de salvar outro usuário.

        // garante tambem que a profile não foi criada
        verify(profileService, never())
                .createUserProfile(any(User.class));

    }

}
