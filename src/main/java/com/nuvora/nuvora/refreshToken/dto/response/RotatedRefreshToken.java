package com.nuvora.nuvora.refreshToken.dto.response;

import com.nuvora.nuvora.user.entity.User;

public record RotatedRefreshToken(
        String rawToken,
        User user
) {
}
