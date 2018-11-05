package com.withkid.auth.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> generateToken(@RequestBody User user) throws Exception {
		return userService.login(user);
	}

	@PostMapping(path = "/accessToken")
	public ResponseEntity<String> getToken(@RequestHeader("refresh-token") String refreshToken) throws Exception {
		String accessToken = null;
		try {
			if (jwtService.thisRefreshTokenUsable(refreshToken)) {
				String email = (String) jwtService.getBody(refreshToken).getBody().get("email");
				HashMap<String, Object> claims = userService.getAccessTokenClaims(email);
				accessToken = jwtService.createAccessToken(claims);
			}
		} catch (RefreshTokenExpireDateUpdatePeriodException e) {
			refreshToken = jwtService.updateRefreshToken(refreshToken);
			return  ResponseEntity.ok().header(refreshHeader, refreshToken).body("Refresh token publishing Done");
		}
		return ResponseEntity.ok().header(jwt_header, accessToken).body("Access token publishing Done");
	}
}
