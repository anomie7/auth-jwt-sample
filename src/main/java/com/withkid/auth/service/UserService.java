package com.withkid.auth.service;

import java.util.HashMap;
import java.util.Optional;

import com.withkid.auth.exception.DuplicatedEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withkid.auth.domain.User;
import com.withkid.auth.exception.PasswordNotMatchException;
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

		user.passwordToHash();
		User findUser = this.findPasswordMatchedUser(user);
		
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

	private User findPasswordMatchedUser(User user) {
		User findUser = this.findEmailMatchedUser(user.getEmail());
		if(user.getPassword().equals(findUser.getPassword())) {
			return findUser;
		}else {
			throw new PasswordNotMatchException();
		}
	}
	
	public HashMap<String, Object> getAccessTokenClaims(String email) {
		User user = this.findEmailMatchedUser(email);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("email", user.getEmail());
		claims.put("id", user.getId());
		return claims;
	}

	public User signUp(User user) throws Exception {
		Optional<User> findUserOpt = userRepository.findByEmail(user.getEmail());
		User newUser;

		if(findUserOpt.isPresent()) {
			throw new DuplicatedEmailException();
		}else {
			user.passwordToHash();
			 newUser = userRepository.save(user);
		}
		return newUser;
	}

	public User findEmailMatchedUser(String email){
		Optional<User> findUserOpt = userRepository.findByEmail(email);
		User findUser = findUserOpt.orElseThrow(UserNotFoundException::new);
		return findUser;
	}

}
