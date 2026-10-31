package com.nulvora.nulvora.helper;

import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.category.repository.CategoryRepository;
import com.nulvora.nulvora.profile.repository.ProfileRepository;
import com.nulvora.nulvora.user.entity.User;
import com.nulvora.nulvora.user.repository.UserRepository;
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
    private final CategoryRepository categoryRepository;


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

    // verificar se existe pelo cpf
    public boolean existsByCpf(String cpf){
        return profileRepository.existsByCpf(cpf);
    }



    // verificar se eixste categoria pelo id
    public Category getCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Categoria não encontrada")
                );
    }


}
