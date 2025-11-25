package com.hipradeep.code.controlers;

import com.hipradeep.code.config.CustomUserDetailsService;
import com.hipradeep.code.config.JwtUtil;
import com.hipradeep.code.dto.AuthRequest;
import com.hipradeep.code.dto.AuthResponse;
import com.hipradeep.code.entity.User;
import com.hipradeep.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    //{
    //    "username":"admin",
    //    "password":"password"
    //}
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        Authentication authentication = null;
        try {

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

            authentication = authenticationManager.authenticate(authToken);

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        // Extract UserDetails directly from the authenticated object
        final UserDetails userDetails =(UserDetails) authentication.getPrincipal();

        //Redundant, actually calling twice
        //final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        //For Additional Details
//        User userEntity = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }


}

