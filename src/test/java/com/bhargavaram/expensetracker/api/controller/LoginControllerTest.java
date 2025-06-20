package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Users;
import com.bhargavaram.expensetracker.api.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .standaloneSetup(loginController)
                .build();
    }


    @Test
    public void loginUser_Should_Return_Token_When_Credentials_Are_Valid() throws Exception {
        Users existingUser = new Users();
        existingUser.setEmail("ram123");
        existingUser.setPassword("pass123");

        Users user = new Users();
        user.setEmail("ram123");
        user.setPassword("pass123");

        String token = "sample_token_123";
        HashMap<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "User logged in successfully!");


        Mockito.when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));
        Mockito.when(jwtUtil.generateToken(user.getEmail())).thenReturn(token);
        Mockito.when(passwordEncoder.matches(user.getPassword(), existingUser.getPassword())).thenReturn(true);

        mockMvc.perform(post("/login/user")
                        .contentType("application/json")
                        .content("{\"email\": \"ram123\", \"password\": \"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"sample_token_123\",\"message\":\"User logged in successfully!\"}"));

    }

    @Test
    public void login_User_Should_Return_Unauthorized_When_Credentials_Are_Invalid() throws Exception {
        Users user = new Users();
        user.setEmail("ram123");
        user.setPassword("wrong_pass");

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User is unauthorized!");

        Mockito.when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/login/user")
                        .contentType("application/json")
                        .content("{\"email\": \"ram123\", \"password\": \"wrong_pass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"message\":\"User is unauthorized!\"}"));
    }

}