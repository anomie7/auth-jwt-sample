package com.withkid.auth.controller;

import com.withkid.auth.domain.EmailUser;
import com.withkid.auth.service.EmailUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class EmailUserController {

    private EmailUserService emailUserService;

    @PostMapping(path = "/login")
    public ResponseEntity<HashMap<String, String>> generateToken(@RequestBody EmailUser user) throws Exception {
        HashMap<String, String> tokens = emailUserService.login(user);
        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping(path = "/join")
    public ResponseEntity<String> joinUser(@RequestBody EmailUser user) throws Exception {
        emailUserService.signUp(user);
        String result = "Success Join User";
        return ResponseEntity.ok().body(result);
    }
}
