package com.nuvora.nuvora.auth.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
