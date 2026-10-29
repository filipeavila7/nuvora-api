package com.nuvora.nuvora.auth.controller;



import com.nuvora.nuvora.auth.dto.request.AuthRequest;
import com.nuvora.nuvora.auth.dto.response.TokenResponse;
import com.nuvora.nuvora.auth.service.AuthService;
import com.nuvora.nuvora.refreshToken.dto.request.RefreshRequest;
import com.nuvora.nuvora.user.dto.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(
            @Valid @RequestBody UserRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse>  login(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshRequest request
    ) {
        authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }
}
