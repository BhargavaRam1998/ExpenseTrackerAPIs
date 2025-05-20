package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.model.Users;
import com.bhargavaram.expensetracker.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/add")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<HashMap<String, String>> addUser(@RequestBody Users user){
        HashMap<String, String> response = userService.addUser(user);

        if(response.containsKey("token")){
            response.put("message", "User created successfully!");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
