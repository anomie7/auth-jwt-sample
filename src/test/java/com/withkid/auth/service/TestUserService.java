package com.withkid.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.withkid.auth.domain.User;
import com.withkid.auth.exception.UserNotFoundException;
import com.withkid.auth.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	@Autowired
	private JwtService jwtService;
	
	private User olderUser = new User(null, "depromeet@older.com", "password5");
	
	@Before
	public void signIn() {
		userService.signUp(olderUser);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testNewUserLogin() throws Exception {
		User newUser = new User(null, "depromeet2@new.com", "password5");
		HashMap<String, String> response = userService.login(newUser);
		Object email = jwtService.getBody(response.get("accessToken")).getBody().get("email");
		
		User findUser = userRepository.findByEmail(newUser.getEmail());
		assertThat(findUser).isEqualTo(newUser);
		assertThat(email).isEqualTo(newUser.getEmail());
	}
	
	@Test
	public void testOlderUserLogin() throws Exception {
		userService.login(olderUser);
		
		HashMap<String, String> response = userService.login(olderUser);
		Object email = jwtService.getBody(response.get("accessToken")).getBody().get("email");
		User findUser = userRepository.findByEmail(olderUser.getEmail());
		assertThat(findUser.getEmail()).isEqualTo(email);
	}

}
