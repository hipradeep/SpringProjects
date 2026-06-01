package com.hipradeep.code.service;

import com.hipradeep.code.entity.UserEntity;
import com.hipradeep.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isResourceOwner(Long id, String username) {
        if (id == null || username == null) {
            return false;
        }
        Optional<UserEntity> userOpt = userRepository.findById(id);
        return userOpt.map(user -> username.equals(user.getUsername())).orElse(false);
    }
}
