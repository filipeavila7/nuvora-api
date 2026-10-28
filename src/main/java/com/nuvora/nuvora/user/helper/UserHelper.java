package com.nuvora.nuvora.user.helper;

import com.nuvora.nuvora.user.repository.UserRepository;
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
