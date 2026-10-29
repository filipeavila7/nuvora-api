package com.nuvora.nuvora.refreshToken.service;

import com.nuvora.nuvora.refreshToken.dto.response.RotatedRefreshToken;
import com.nuvora.nuvora.refreshToken.entity.RefreshToken;
import com.nuvora.nuvora.refreshToken.repository.RefreshTokenRepository;
import com.nuvora.nuvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String createRefreshToken(User user) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(hashToken(rawToken));
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenExpiration)
        );
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Transactional
    public User validateRefreshToken(String rawToken) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(hashToken(rawToken))
                .orElseThrow(this::invalidToken);

        if (refreshToken.isRevoked()
                || !refreshToken.getExpiryDate().isAfter(Instant.now())) {
            throw invalidToken();
        }

        return refreshToken.getUser();
    }

    @Transactional
    public void revokeRefreshToken(String rawToken) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(hashToken(rawToken))
                .orElseThrow(this::invalidToken);

        refreshToken.setRevoked(true);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 não está disponível", exception
            );
        }
    }



    @Transactional
    public RotatedRefreshToken rotateRefreshToken(String rawToken) {
        RefreshToken oldToken = refreshTokenRepository
                .findByTokenForUpdate(hashToken(rawToken))
                .orElseThrow(this::invalidToken);

        if (oldToken.isRevoked()
                || !oldToken.getExpiryDate().isAfter(Instant.now())) {
            throw invalidToken();
        }

        User user = oldToken.getUser();

        oldToken.setRevoked(true);

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String newRawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(hashToken(newRawToken));
        newToken.setUser(user);
        newToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenExpiration)
        );
        newToken.setRevoked(false);

        refreshTokenRepository.save(newToken);

        return new RotatedRefreshToken(newRawToken, user);
    }



    private ResponseStatusException invalidToken() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Refresh token inválido, expirado ou revogado"
        );
    }
}