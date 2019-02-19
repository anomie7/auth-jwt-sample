package com.withkid.auth.controller;


import com.google.cloud.firestore.Firestore;
import com.withkid.auth.repository.FirestoreRepository;
import com.withkid.auth.service.FacebookService;
import lombok.AllArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
public class FacebookController {

    private FacebookService facebookService;
    private FirestoreRepository firestoreRepository;

    @GetMapping(path = "/authURL")
    public String createFacebookAuthorizationURL(String temporaryCode) throws IOException {
        final String facebookAuthorizationURL = facebookService.createFacebookAuthorizationURL(temporaryCode);
        Firestore db = firestoreRepository.getFireStore();
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", "");
        data.put("refreshToken", "");
        data.put("createdAt", new LocalDateTime().now().toString());
        db.collection("temporary-login-token").document(temporaryCode).set(data);
        return facebookAuthorizationURL;
    }

    @GetMapping("/facebookToken")
    public ResponseEntity<String> createFacebookAccessToken(@RequestParam("code") String code, @RequestParam("temporaryCode") String temporaryCode) throws Exception {
        HashMap<String, String> tokens = facebookService.login(code, temporaryCode);
        firestoreRepository.saveTokens(tokens, temporaryCode);
        return ResponseEntity.ok().body("token generate success!");
    }

    @GetMapping("/authtoken")
    public ResponseEntity<Map<String, Object>> findAccessTokenAndRefreshToken(@RequestParam("temporaryCode") String temporaryCode) throws InterruptedException, ExecutionException, IOException {
        Map<String, Object> tokens = firestoreRepository.findAccessTokenAndRefreshToken(temporaryCode);
        return ResponseEntity.ok().body(tokens);
    }

}
