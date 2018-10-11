package com.depromeet.team5.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.team5.User;

@RestController
public class AuthController {
	@Autowired
	private JwtService jwtService;
	final String jwt_header = "Authorization";

	@PostMapping(path = "/login")
	public ResponseEntity<String> generateToken(@RequestBody User user) throws Exception {
		return jwtService.userHandler(user);
	}

	@PostMapping(path = "/auth")
	public ResponseEntity<String> getToken(HttpServletRequest request) throws Exception {
		String jwt = request.getHeader(jwt_header);
		if (!jwtService.isUsable(jwt)) {
			throw new Exception("is not usable jwt");
		}
		return null;
	}

}
