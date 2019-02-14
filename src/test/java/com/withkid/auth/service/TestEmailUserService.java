package com.withkid.auth.service;

import com.withkid.auth.domain.EmailUser;
import com.withkid.auth.domain.User;
import com.withkid.auth.exception.PasswordNotMatchException;
import com.withkid.auth.exception.UserNotFoundException;
import com.withkid.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TestEmailUserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailUserService emailUserService;
	@Autowired
	private JwtService jwtService;
	
	private EmailUser olderUser = new EmailUser(null, "depromeet@older.com", "password5");
	private EmailUser passwordNotMatchUser = new EmailUser(null, "depromeet@older.com", "worngPassword");

	@Before
	public void saveUser() throws Exception {
		emailUserService.signUp(olderUser);
	}

	@Test
	public void testSignUp() throws Exception{
		emailUserService.signUp(new EmailUser(null, "newUser@naver.com", "passwordnew123"));
	}

	@Test(expected = Exception.class)
	public void testExistEmailSignUp() throws Exception {
		emailUserService.signUp(passwordNotMatchUser);
	}

	@Test(expected=UserNotFoundException.class)
	public void testNotExistUserLogin() throws Exception {
		EmailUser newUser = new EmailUser(null, "depromeet2@new.com", "password5");
		HashMap<String, String> response = emailUserService.login(newUser);
	}
	
	@Test
	public void testExistUserLogin() throws Exception {
		emailUserService.login(olderUser);
		
		HashMap<String, String> response = emailUserService.login(olderUser);
		Object email = jwtService.getBody(response.get("accessToken")).getBody().get("email");
		Optional<User> findUserOpt = userRepository.findByEmail(olderUser.getEmail());
		assertThat(findUserOpt.get().getEmail()).isEqualTo(email);
	}
	
	@Test(expected=PasswordNotMatchException.class)
	public void testPasswordNotMatchUserLogin() throws Exception {

		emailUserService.login(passwordNotMatchUser);
	}

}
