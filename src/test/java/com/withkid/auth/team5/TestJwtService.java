package com.withkid.auth.team5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.withkid.auth.exception.RefreshTokenExpireDateUpdatePeriodException;
import com.withkid.auth.jwt.JwtService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJwtService {
	@Autowired
	private JwtService jwtService;

	private String test_jwt;

	private String test_refresh_token;

	@Before
	public void CreateKey() throws UnsupportedEncodingException {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", "depromeet@tract4.com");
		test_refresh_token = jwtService.createRefreshToken(claims);
		claims.put("Id", 1);
		test_jwt = jwtService.createAccessToken(claims);
	}

	@Test
	public void testCreateRefreshToken() {
		assertThat(jwtService.thisRefreshTokenUsable(test_refresh_token)).isEqualTo(true);
	}

	@Test
	public void testThisAccessTokenUsable() {
		assertThat(jwtService.thisAccessTokenUsable(test_jwt)).isEqualTo(true);
	}

	@Test
	public void testThisRefreshTokenUsable() {
		assertThat(jwtService.thisRefreshTokenUsable(test_refresh_token)).isEqualTo(true);
	}

	@Test
	public void testSetExpRefreshToken() {
		String refresh_token = jwtService.updateRefreshToken(test_refresh_token);
		Date before = jwtService.getBody(test_refresh_token).getBody().getExpiration();
		Date after = jwtService.getBody(refresh_token).getBody().getExpiration();
		assertEquals(true, before.before(after));
	}
	
	@Test(expected=RefreshTokenExpireDateUpdatePeriodException.class)
	public void expectedRefreshTokenExpireDateUpdatePeriodException() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expDate = LocalDateTime.now().plusDays(10);
		if(now.plusDays(4).isAfter(expDate.minusWeeks(1))){
			throw new RefreshTokenExpireDateUpdatePeriodException();
		}
	}
}
