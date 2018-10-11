package com.depromeet.team5;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.depromeet.team5.jwt.JwtService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Test
	public void testGenerateJwt() throws Exception {
		User user = new User(null, "depromeet@Track5.com", "password5");
		ResponseEntity<String> response = jwtService.userHandler(user);
		String email = jwtService.getEmail(response);
		
		User findUser = userRepository.findByEmail(user.getEmail());
		assertThat(findUser).isEqualTo(user);
		assertThat(email).isEqualTo(user.getEmail());
	}

}
