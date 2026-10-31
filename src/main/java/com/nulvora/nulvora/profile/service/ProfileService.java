package com.nulvora.nulvora.profile.service;

import com.nulvora.nulvora.profile.entity.Profile;
import com.nulvora.nulvora.profile.repository.ProfileRepository;
import com.nulvora.nulvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;


    public void createUserProfile(User user){
        Profile profile = new Profile(null, null, user);

        profileRepository.save(profile);
    }
}
