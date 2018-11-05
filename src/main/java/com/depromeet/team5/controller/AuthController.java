package com.depromeet.team5.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.team5.User;
import com.depromeet.team5.UserService;
import com.depromeet.team5.jwt.JwtService;

@RestController 
public class AuthController {
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	final String jwt_header = "Authorization";

	@PostMapping(path = "/login")
	public ResponseEntity<String> generateToken(@RequestBody User user) throws Exception {
		return userService.login(user);
	}

	@PostMapping(path = "/auth")
	public ResponseEntity<String> getToken(HttpServletRequest request) throws Exception {
		String jwt = request.getHeader(jwt_header);
		if (!jwtService.thisAccessTokenUsable(jwt)) {
			throw new Exception("is not usable jwt");
		}
		return ResponseEntity.ok().body("Success! useable jwt");
	}
}
