package com.nuvora.nuvora.auth.service;



import com.nuvora.nuvora.auth.dto.request.AuthRequest;
import com.nuvora.nuvora.auth.dto.response.TokenResponse;
import com.nuvora.nuvora.jwt.JwtService;
import com.nuvora.nuvora.user.dto.request.UserRequest;
import com.nuvora.nuvora.user.entity.User;
import com.nuvora.nuvora.user.entity.UserRole;
import com.nuvora.nuvora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse register(UserRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email já cadastrado"
            );
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setUserRole(UserRole.CLIENT);

        userRepository.save(user);

        return generateToken(user);
    }


    public TokenResponse login(AuthRequest request) {
        String email = normalizeEmail(request.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Email ou senha inválidos"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha inválidos"
            );
        }

        return generateToken(user);
    }

    private TokenResponse generateToken(User user) {
        String token = jwtService.generateAccessToken(
                user.getEmail(),
                user.getUserRole().name()
        );

        return new TokenResponse(token, "Bearer");
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}