package com.hipradeep.code.services.impl;

import com.hipradeep.code.entities.UserProfile;
import com.hipradeep.code.repositories.UserProfileRepository;
import com.hipradeep.code.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;



    @Override
    public UserProfile uploadProfile(UserProfile user) {
        return this.userProfileRepository.save(user);
    }
}
