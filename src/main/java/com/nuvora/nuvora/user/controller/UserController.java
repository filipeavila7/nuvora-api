package com.nuvora.nuvora.user.controller;

import com.nuvora.nuvora.user.dto.response.UserResponse;
import com.nuvora.nuvora.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(){
        return ResponseEntity.ok(userService.getMe());
    }
}
