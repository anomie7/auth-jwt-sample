package com.withkid.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.withkid.auth.domain.User;
import com.withkid.auth.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	@Autowired
	private JwtService jwtService;
	
	@Test
	public void testNewUserGenerateJwt() throws Exception {
		User user = new User(null, "depromeet@Track5.com", "password5");
		HashMap<String, String> response = userService.login(user);
		Object email = jwtService.getBody(response.get("accessToken")).getBody().get("email");
		
		User findUser = userRepository.findByEmail(user.getEmail());
		assertThat(findUser).isEqualTo(user);
		assertThat(email).isEqualTo(user.getEmail());
	}
	
	@Test
	public void testOlderUserGenerateJwt() throws Exception {
		User user = new User(null, "depromeet@Track5.com", "password5");
		userService.login(user);
		
		HashMap<String, String> response = userService.login(user);
		Object email = jwtService.getBody(response.get("accessToken")).getBody().get("email");
		User findUser = userRepository.findByEmail(user.getEmail());
		assertThat(findUser.getEmail()).isEqualTo(email);
	}

}
