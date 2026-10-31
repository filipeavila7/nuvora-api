package com.nulvora.nulvora.user.service;

import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.user.dto.request.UserRequest;
import com.nulvora.nulvora.user.dto.response.UserResponse;
import com.nulvora.nulvora.user.entity.User;
import com.nulvora.nulvora.user.helper.UserHelper;
import com.nulvora.nulvora.user.mapper.UserMapper;
import com.nulvora.nulvora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserHelper userHelper;
    private final PasswordEncoder passwordEncoder;
    private final GlobalHelperService globalHelperService;


    // pegar logado
    public UserResponse getMe(){
        return userMapper.toUserResponse(globalHelperService.getLoggedUser());
    }

    // criar usuário
    public UserResponse createUser(UserRequest request){
        // verifica se ja existe usuario com o email enviado
        if (userHelper.existsByEmail(request.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nome de usuário ou email já está em uso"
            );
        }

        // cria o objeto
        User user = userMapper.toEntity(request);

        // criptografa a senha
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        // salva
        userRepository.save(user);

        // retorna em formato de response
        return userMapper.toUserResponse(user);


    }


}
