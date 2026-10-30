package com.nuvora.nuvora.profile;


import com.nuvora.nuvora.profile.entity.Profile;
import com.nuvora.nuvora.profile.repository.ProfileRepository;
import com.nuvora.nuvora.profile.service.ProfileService;
import com.nuvora.nuvora.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;


    @InjectMocks
    private ProfileService profileService;


    @Test
    void shouldCreateProfile(){
        User user = new User();

        // executa o metodo
        profileService.createUserProfile(user);

        // capturar o argumento enviado no save(), ele ainda não capturou nada
        // capturador que sabe trabalhar com objetos do tipo Profile
        ArgumentCaptor<Profile> argumentCaptor = ArgumentCaptor.forClass(Profile.class);

        // verifica se o metodo save foi chamado, e captura o argumento que foi passado no save
        // .capture() captura argumentos passados a metodos
        verify(profileRepository).save(argumentCaptor.capture());

        // pegar o valor do argumento que foi capturado
        Profile savedProfile = argumentCaptor.getValue();

        // verifca se o usuario bate com o usuario de savedProfile
        assertEquals(user, savedProfile.getUser());

    }

}
