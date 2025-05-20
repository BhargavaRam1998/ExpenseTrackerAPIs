package com.bhargavaram.expensetracker.api.service;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Users;
import com.bhargavaram.expensetracker.api.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public HashMap<String, String> addUser(Users user) {

        HashMap<String, String> response = new HashMap<>();

        Optional<Users> existingUser = userRepo.findByEmail(user.getEmail());

        if(existingUser.isPresent()){
            response.put("Error!!", "User already exists");
            return response;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        response.put("token", token);
        return response;
    }
}
