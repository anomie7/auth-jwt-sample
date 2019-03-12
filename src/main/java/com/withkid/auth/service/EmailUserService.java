package com.withkid.auth.service;

import com.withkid.auth.domain.EmailUser;
import com.withkid.auth.domain.User;
import com.withkid.auth.exception.DuplicatedEmailException;
import com.withkid.auth.exception.PasswordNotMatchException;
import com.withkid.auth.exception.UserNotFoundException;
import com.withkid.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class EmailUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	// 로그인 로직
	public HashMap<String, String> login(EmailUser user) throws Exception {
		String aceessToken = null;
		String refreshToken = null;

		user.passwordToHash();
		User findUser = this.findPasswordMatchedUser(user);
		
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("user-type", "E");
		claims.put("email", findUser.getEmail());
		refreshToken = jwtService.createRefreshToken(claims);
		claims.put("user-id", findUser.getId());
		aceessToken = jwtService.createAccessToken(claims);
		HashMap<String, String> res = new HashMap<>();
		res.put("accessToken", aceessToken);
		res.put("refreshToken", refreshToken);
		return res;
	}

	private User findPasswordMatchedUser(EmailUser user) {
		EmailUser findUser = (EmailUser) this.findEmailMatchedUser(user.getEmail());
		if(user.getPassword().equals(findUser.getPassword())) {
			return findUser;
		}else {
			throw new PasswordNotMatchException();
		}
	}
	
	public HashMap<String, Object> getAccessTokenClaims(String email) {
		EmailUser user = (EmailUser) this.findEmailMatchedUser(email);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("email", user.getEmail());
		claims.put("user-id", user.getId());
		return claims;
	}

	public EmailUser signUp(EmailUser user) throws Exception {

		if(user.getPassword() == null || user.getPassword().isEmpty()){
			throw new Exception("패스워드를 입력해주세요.");
		}

		Optional<User> findUserOpt = userRepository.findByEmail(user.getEmail());
		EmailUser newUser;

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
