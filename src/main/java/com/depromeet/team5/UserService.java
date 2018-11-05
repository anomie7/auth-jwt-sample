package com.depromeet.team5;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.depromeet.team5.jwt.JwtService;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	// 로그인 로직
	public ResponseEntity<String> login(User user) throws Exception {
		String aceessToken = null;
		String refreshToken = null;

		User findUser = userRepository.findByEmail(user.getEmail());
		if (findUser == null) { 
			findUser = userRepository.save(user);
		}
		
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", findUser.getEmail());
		refreshToken = jwtService.createRefreshToken(claims);
		claims.put("id", findUser.getId());
		aceessToken = jwtService.createAccessToken(claims);
		return ResponseEntity.ok().header("Authentication", aceessToken).header("refresh_token", refreshToken).body("Done");
	}
}
