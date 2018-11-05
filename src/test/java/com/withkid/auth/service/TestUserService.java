package com.withkid.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.withkid.auth.domain.User;
import com.withkid.auth.repository.UserRepository;
import com.withkid.auth.service.JwtService;
import com.withkid.auth.service.UserService;

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
		ResponseEntity<String> response = userService.login(user);
		String email = jwtService.getEmail(response);
		
		User findUser = userRepository.findByEmail(user.getEmail());
		assertThat(findUser).isEqualTo(user);
		assertThat(email).isEqualTo(user.getEmail());
	}
	
	@Test
	public void testOlderUserGenerateJwt() throws Exception {
		User user = new User(null, "depromeet@Track5.com", "password5");
		userService.login(user);
		
		ResponseEntity<String> response2 = userService.login(user);
		String email = jwtService.getEmail(response2);
		User findUser = userRepository.findByEmail(user.getEmail());
		assertThat(findUser.getEmail()).isEqualTo(email);
	}

}
