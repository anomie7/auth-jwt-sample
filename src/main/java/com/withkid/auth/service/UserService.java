package com.withkid.auth.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withkid.auth.domain.User;
import com.withkid.auth.exception.UserNotFoundException;
import com.withkid.auth.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	// 로그인 로직
	public HashMap<String, String> login(User user) throws Exception {
		String aceessToken = null;
		String refreshToken = null;

		Optional<User> findUserOpt = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
		User findUser = findUserOpt.orElseThrow(UserNotFoundException::new);
		
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", findUser.getEmail());
		refreshToken = jwtService.createRefreshToken(claims);
		claims.put("id", findUser.getId());
		aceessToken = jwtService.createAccessToken(claims);
		HashMap<String, String> res = new HashMap<>();
		res.put("accessToken", aceessToken);
		res.put("refreshToken", refreshToken);
		return res;
	}
	
	public HashMap<String, Object> getAccessTokenClaims(String email) {
		Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
		User user = userOpt.orElseThrow(UserNotFoundException::new);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("email", user.getEmail());
		claims.put("id", user.getId());
		return claims;
	}
	
	public User signUp(User user) {
		User findUser;
		findUser = userRepository.save(user);
		return findUser;
	}
}
