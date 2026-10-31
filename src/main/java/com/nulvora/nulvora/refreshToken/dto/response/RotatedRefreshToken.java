package com.nulvora.nulvora.refreshToken.dto.response;

import com.nulvora.nulvora.user.entity.User;

public record RotatedRefreshToken(
        String rawToken,
        User user
) {
}
