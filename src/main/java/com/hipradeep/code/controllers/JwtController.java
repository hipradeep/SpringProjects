package com.hipradeep.code.controllers;

import com.hipradeep.code.helper.JwtUtil;
import com.hipradeep.code.models.JwtAuthResponse;
import com.hipradeep.code.models.JwtRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

     @Autowired
    @Qualifier("authenticationManagerBean")
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthResponse> generateToken(@RequestBody JwtRequest request) throws Exception {


        this.authentication(request.getUsername(), request.getPassword());
        System.out.println("HI");
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

        String token = this.jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    private void authentication(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {

            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            System.out.println("Bad Credentials   !!!");
            throw new Exception("Bad Credentials   !!");
        }
    }
}
