package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Users;
import com.bhargavaram.expensetracker.api.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login/user")
    public ResponseEntity<HashMap<String, String>> loginUser(@RequestBody Users user){
        HashMap<String, String> response = new HashMap<>();
        Optional<Users> existingUser = userRepo.findByEmail(user.getEmail());

        if(existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())){
            String token = jwtUtil.generateToken(user.getEmail());
            response.put("token", token);
            response.put("message", "User logged in successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "User is unauthorized!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
