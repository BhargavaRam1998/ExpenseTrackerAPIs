package com.bhargavaram.expensetracker.api.service;


import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Users;
import com.bhargavaram.expensetracker.api.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;



    @Test
    public void addUser_Should_Add_User() {

        Users user = new Users();
        user.setEmail("ram123");
        user.setPassword("pass123");

        HashMap<String, String> response = new HashMap<>();
        String token = "sample_token";
        response.put("token", token);

        Mockito.when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword123");
        Mockito.when(userRepo.save(user)).thenReturn(user);
        Mockito.when(jwtUtil.generateToken(user.getEmail())).thenReturn(token);

        HashMap<String, String> result = userService.addUser(user);

        assert result.containsKey("token");
        assert result.get("token").equals(token);
        assert result.size() == 1;

    }

    @Test
    public void addUser_Should_Return_Error_When_User_Exists() {

        Users user = new Users();
        user.setEmail("ram123");
        user.setPassword("pass123");

        HashMap<String, String> response = new HashMap<>();
        response.put("Error!!", "User already exists");

        Mockito.when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        HashMap<String, String> result = userService.addUser(user);

        assert result.containsKey("Error!!");
        assert result.get("Error!!").equals("User already exists");
        assert result.size() == 1;

    }

}