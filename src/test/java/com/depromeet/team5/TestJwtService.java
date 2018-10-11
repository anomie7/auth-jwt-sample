package com.depromeet.team5;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.depromeet.team5.jwt.JwtService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJwtService {
	
	@Autowired
	private JwtService jwtService;
	
	private String test_jwt;
	
	@Before
	public void CreateKey() throws UnsupportedEncodingException {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", "depromeet@tract4.com");
		test_jwt = jwtService.create(claims);
	}
	
	@Test
	public void testIsUsable() {
	  assertThat(jwtService.isUsable(test_jwt)).isEqualTo(true);
	}
}
