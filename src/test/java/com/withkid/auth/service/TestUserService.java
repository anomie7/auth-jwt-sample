package com.withkid.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.withkid.auth.domain.User;
import com.withkid.auth.exception.PasswordNotMatchException;
import com.withkid.auth.exception.UserNotFoundException;
import com.withkid.auth.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TestUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	@Autowired
	private JwtService jwtService;
	
	private User olderUser = new User(null, "depromeet@older.com", "password5");
	private User passwordNotMatchUser = new User(null, "depromeet@older.com", "worngPassword");

	@Before
	public void saveUser() throws Exception {
		userService.signUp(olderUser);
	}

	@Test
	public void testSignUp() throws Exception{
		userService.signUp(new User(null, "newUser@naver.com", "passwordnew123"));
	}

	@Test(expected = Exception.class)
	public void testExistEmailSignUp() throws Exception {
		userService.signUp(passwordNotMatchUser);
	}

	@Test(expected=UserNotFoundException.class)
	public void testNotExistUserLogin() throws Exception {
		User newUser = new User(null, "depromeet2@new.com", "password5");
		HashMap<String, String> response = userService.login(newUser);
	}
	
	@Test
	public void testExistUserLogin() throws Exception {
		userService.login(olderUser);
		
		HashMap<String, String> response = userService.login(olderUser);
		Object email = jwtService.getBody(response.get("accessToken")).getBody().get("email");
		Optional<User> findUserOpt = userRepository.findByEmail(olderUser.getEmail());
		assertThat(findUserOpt.get().getEmail()).isEqualTo(email);
	}
	
	@Test(expected=PasswordNotMatchException.class)
	public void testPasswordNotMatchUserLogin() throws Exception {

		userService.login(passwordNotMatchUser);
	}

}
