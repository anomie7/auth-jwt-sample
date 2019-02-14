package com.withkid.auth.controller;


import com.withkid.auth.service.FacebookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class FacebookController {

    private FacebookService facebookService;

    @GetMapping(path = "/authURL")
    public String createFacebookAuthorizationURL(){
        final String facebookAuthorizationURL = facebookService.createFacebookAuthorizationURL();
        return facebookAuthorizationURL;
    }

    @GetMapping("/facebookToken")
    public ResponseEntity<HashMap<String, String>> createFacebookAccessToken(@RequestParam("code") String code) throws Exception {
        HashMap<String, String> tokens = facebookService.login(code);
        return ResponseEntity.ok().body(tokens);
    }

}
