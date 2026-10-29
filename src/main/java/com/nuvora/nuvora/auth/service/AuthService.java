package com.nuvora.nuvora.auth.service;



import com.nuvora.nuvora.auth.dto.request.AuthRequest;
import com.nuvora.nuvora.auth.dto.response.TokenResponse;
import com.nuvora.nuvora.helper.GlobalHelperService;
import com.nuvora.nuvora.jwt.JwtService;
import com.nuvora.nuvora.profile.service.ProfileService;
import com.nuvora.nuvora.refreshToken.dto.response.RotatedRefreshToken;
import com.nuvora.nuvora.refreshToken.service.RefreshTokenService;
import com.nuvora.nuvora.user.dto.request.UserRequest;
import com.nuvora.nuvora.user.entity.User;
import com.nuvora.nuvora.user.entity.UserRole;
import com.nuvora.nuvora.user.mapper.UserMapper;
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
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final ProfileService profileService;

    public TokenResponse register(UserRequest request) {
        String email = normalizeEmail(request.email());

        // verifica se o email ja foi usado
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email já cadastrado"
            );
        }


        User user = userMapper.toEntity(request);

        // criptografa a senha
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        // cria uma profile pro usuario
        profileService.createUserProfile(user);

        userRepository.save(user);

        // ja retorna o jwt
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
        String accessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getUserRole().name()
        );

        String refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer"
        );
    }

    public void logout(String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
    }

    public TokenResponse refresh(String rawRefreshToken) {
        RotatedRefreshToken rotated =
                refreshTokenService.rotateRefreshToken(rawRefreshToken);

        String accessToken = jwtService.generateAccessToken(
                rotated.user().getEmail(),
                rotated.user().getUserRole().name()
        );

        return new TokenResponse(
                accessToken,
                rotated.rawToken(),
                "Bearer"
        );
    }



    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}