package com.nuvora.nuvora.helper;

import com.nuvora.nuvora.profile.repository.ProfileRepository;
import com.nuvora.nuvora.user.entity.User;
import com.nuvora.nuvora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GlobalHelperService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    // pegar user logado no contexto da aplicação
    public User getLoggedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));
    }

    // validar cpf
    public boolean isValidCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        // Rejeita CPFs com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        for (int i = 9; i < 11; i++) {
            int sum = 0;

            for (int j = 0; j < i; j++) {
                sum += (cpf.charAt(j) - '0') * ((i + 1) - j);
            }

            int remainder = (sum * 10) % 11;
            int digit = remainder == 10 ? 0 : remainder;

            if (digit != cpf.charAt(i) - '0') {
                return false;
            }
        }

        return true;
    }


    public boolean existsByCpf(String cpf){
        return profileRepository.existsByCpf(cpf);
    }


}
