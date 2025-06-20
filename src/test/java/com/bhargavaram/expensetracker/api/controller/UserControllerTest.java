package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.exception.GlobalExceptionHandler;
import com.bhargavaram.expensetracker.api.model.Users;
import com.bhargavaram.expensetracker.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void addUser_Should_Add_User() throws Exception {
        Users user = new Users();
        user.setName("Bhargav");
        user.setPassword("pass123");
        user.setEmail("bhargav123@gmail.com");

        HashMap<String, String> response = new HashMap<>();
        response.put("token","sampleToken123");

        Mockito.when(userService.addUser(user)).thenReturn(response);

        mockMvc.perform(post("/add/user")
                .contentType("application/json")
                .content("{\"name\": \"Bhargav\", \"password\": \"pass123\", \"email\": \"bhargav123@gmail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"token\":\"sampleToken123\",\"message\":\"User created successfully!\"}"));

    }

    @Test
    public void addUser_Should_Not_Add_User_If_Already_Exists() throws Exception {


        HashMap<String, String> response = new HashMap<>();
        response.put("Error!!", "User already exists");

        Mockito.when(userService.addUser(Mockito.any(Users.class))).thenReturn(response);


        mockMvc.perform(post("/add/user")
                        .contentType("application/json")
                        .content("{\"name\": \"Bhargav\", \"password\": \"pass123\", \"email\": \"bhargav123@gmail.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"Error!!\":\"User already exists\"}"));

    }

}