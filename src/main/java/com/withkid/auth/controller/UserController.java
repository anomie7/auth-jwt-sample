package com.withkid.auth.controller;

import com.withkid.auth.domain.User;
import com.withkid.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<HashMap<String, String>> generateToken(@RequestBody User user) throws Exception {
        HashMap<String, String> tokens = userService.login(user);
        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping(path = "/join")
    public ResponseEntity<String> joinUser(@RequestBody User user) throws Exception {
        userService.signUp(user);
        String result = "Success Join User";
        return ResponseEntity.ok().body(result);
    }

}
