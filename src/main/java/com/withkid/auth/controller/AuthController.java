package com.withkid.auth.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.auth.domain.User;
import com.withkid.auth.exception.RefreshTokenExpireDateUpdatePeriodException;
import com.withkid.auth.service.JwtService;
import com.withkid.auth.service.UserService;

@RestController
public class AuthController {
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	final String jwt_header = "Authorization";
	final String refreshHeader = "refresh-token";

	@PostMapping(path = "/login")
	public ResponseEntity<HashMap<String, String>> generateToken(@RequestBody User user) throws Exception {
		HashMap<String, String> tokens = userService.login(user);
		return ResponseEntity.ok().body(tokens);
	}

	@PostMapping(path = "/accessToken")
	public ResponseEntity<HashMap<String, String>> getToken(@RequestHeader(jwt_header) String refreshToken) throws Exception {
		String accessToken = null;
		HashMap<String, String> tokens = new HashMap<>();
		try {
			if (jwtService.thisRefreshTokenUsable(refreshToken)) {
				String email = (String) jwtService.getBody(refreshToken).getBody().get("email");
				HashMap<String, Object> claims = userService.getAccessTokenClaims(email);
				accessToken = jwtService.createAccessToken(claims);
			}
		} catch (RefreshTokenExpireDateUpdatePeriodException e) {
			refreshToken = jwtService.updateRefreshToken(refreshToken);
			String email = (String) jwtService.getBody(refreshToken).getBody().get("email");
			HashMap<String, Object> claims = userService.getAccessTokenClaims(email);
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
}
