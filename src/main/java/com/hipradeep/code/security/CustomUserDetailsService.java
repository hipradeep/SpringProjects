package com.hipradeep.code.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class CustomUserDetailsService implements UserDetailsService {



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //User user = this.userRepo.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User ", "Email : "+username, 0));
        if (username.equals("pradeep"))
        return new User("pradeep", "pradeep123", new ArrayList<>());
        else throw new UsernameNotFoundException("User not found");
    }
}
