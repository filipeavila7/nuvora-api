package com.nulvora.nulvora.user.helper;

import com.nulvora.nulvora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHelper {
    private final UserRepository userRepository;

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
