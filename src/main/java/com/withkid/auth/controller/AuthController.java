package com.withkid.auth.controller;

import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.auth.exception.RefreshTokenUpdatePeriodOverException;
import com.withkid.auth.service.JwtService;
import com.withkid.auth.service.EmailUserService;

@RestController
public class AuthController {
	@Autowired
	private JwtService jwtService;

	@Autowired
	private EmailUserService emailUserService;

	final String jwt_header = "Authorization";
	final String refreshHeader = "refresh-token";

	@PostMapping(path = "/accessToken")
	public ResponseEntity<HashMap<String, String>> getToken(@RequestHeader(jwt_header) String refreshToken) throws Exception {
		String accessToken = null;
		HashMap<String, String> tokens = new HashMap<>();
		try {
			if (jwtService.thisRefreshTokenUsable(refreshToken)) {
				String email = (String) jwtService.getBody(refreshToken).getBody().get("email");
				HashMap<String, Object> claims = emailUserService.getAccessTokenClaims(email);
				accessToken = jwtService.createAccessToken(claims);
			}
		} catch (RefreshTokenUpdatePeriodOverException e) {
			refreshToken = jwtService.updateRefreshToken(refreshToken);
			String email = (String) jwtService.getBody(refreshToken).getBody().get("email");
			HashMap<String, Object> claims = emailUserService.getAccessTokenClaims(email);
			accessToken = jwtService.createAccessToken(claims);
			tokens.put("refreshToken", refreshToken);
			tokens.put("accessToken", accessToken);
			return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
		}
		tokens.put("accessToken", accessToken);
		return ResponseEntity.ok().body(tokens);
	}
	
	@GetMapping(path = "/accessToken")
	public ResponseEntity<String> validateToken(@RequestHeader(jwt_header) String accessToken){
		jwtService.thisAccessTokenUsable(accessToken);
		return ResponseEntity.ok().body("this access token valid");
	}
	
	@GetMapping(path = "/userId")
	public ResponseEntity<String> getUserId(@RequestHeader(jwt_header) String accessToken){
		jwtService.thisAccessTokenUsable(accessToken);
		Jws<Claims> body = jwtService.getBody(accessToken);
		String userId = String.valueOf(body.getBody().get("user-id"));
		return ResponseEntity.ok().body(userId);
	}
}
