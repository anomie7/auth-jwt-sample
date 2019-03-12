package com.withkid.auth.service;

import com.withkid.auth.exception.RefreshTokenUpdatePeriodOverException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestJwtService {
	@Autowired
	private JwtService jwtService;

	private String test_access_token;

	private String test_refresh_token;

	@Before
	public void CreateKey() throws UnsupportedEncodingException {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", "depromeet@tract4.com");
		test_refresh_token = jwtService.createRefreshToken(claims);
		claims.put("user-id", 5L);
		test_access_token = jwtService.createAccessToken(claims);
	}

	@Test
	public void testCreateRefreshToken() throws Exception {
		assertThat(jwtService.thisRefreshTokenUsable(test_refresh_token)).isEqualTo(true);
	}

	@Test
	public void testThisAccessTokenUsable() {
		assertThat(jwtService.thisAccessTokenUsable(test_access_token)).isEqualTo(true);
	}

	@Test
	public void testThisRefreshTokenUsable() throws Exception {
		assertThat(jwtService.thisRefreshTokenUsable(test_refresh_token)).isEqualTo(true);
	}

	@Test
	public void testSetExpRefreshToken() {
		String refresh_token = jwtService.updateRefreshToken(test_refresh_token);
		Date before = jwtService.getBody(test_refresh_token).getBody().getExpiration();
		Date after = jwtService.getBody(refresh_token).getBody().getExpiration();
		assertEquals(true, before.before(after));
	}

	@Test(expected = RefreshTokenUpdatePeriodOverException.class)
	public void expectedRefreshTokenExpireDateUpdatePeriodException() throws Exception {
		String sampleRefreshTkn = jwtService
				.createSampleRefreshToken(Date.from(ZonedDateTime.now().plusDays(6).toInstant()));
		jwtService.thisRefreshTokenUsable(sampleRefreshTkn);
	}

	@Test
	public void testUpdateRefreshToken() throws Exception {
		String sampleRefreshTkn = jwtService
				.createSampleRefreshToken(Date.from(ZonedDateTime.now().plusDays(6).toInstant()));
		String updatedToken = null;
		try {
			jwtService.thisRefreshTokenUsable(sampleRefreshTkn);
		}  catch (RefreshTokenUpdatePeriodOverException e) {
			updatedToken = jwtService.updateRefreshToken(sampleRefreshTkn);
		}
		
		Jws<Claims> beforeToken = jwtService.getBody(sampleRefreshTkn);
		Jws<Claims> afterToken = jwtService.getBody(updatedToken);
		assertEquals(beforeToken.getHeader().get("regDate"), afterToken.getHeader().get("regDate"));
		assertEquals(true, beforeToken.getBody().getExpiration().before(afterToken.getBody().getExpiration()));
	}

}
