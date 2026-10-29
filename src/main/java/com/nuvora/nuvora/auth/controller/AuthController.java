package com.nuvora.nuvora.auth.controller;



import com.nuvora.nuvora.auth.dto.request.AuthRequest;
import com.nuvora.nuvora.auth.dto.response.TokenResponse;
import com.nuvora.nuvora.auth.service.AuthService;
import com.nuvora.nuvora.user.dto.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public TokenResponse login(
            @Valid @RequestBody AuthRequest request
    ) {
        return authService.login(request);
    }
}
