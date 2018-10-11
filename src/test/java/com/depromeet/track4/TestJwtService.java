package com.depromeet.track4;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class TestJwtService {
	final private String SECRET_KEY = "depromeet_mini_prj";
	final private Date exp = new Date(System.currentTimeMillis() + 36000000);
	private String test_jwt;
	
	@Before
	public void CreateKey() throws UnsupportedEncodingException {
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("email", "depromeet@tract4.com");
		claims.put("role",  "ADMIN");
		test_jwt = Jwts.builder()
					   .setHeaderParam("typ", "JWT")
					   .setHeaderParam("regDate", System.currentTimeMillis())
					   .setSubject("email")
					   .setClaims(claims)
					   .setExpiration(exp)
					   .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes("UTF-8"))
					   .compact();
	}
	
	@Test
	public void test2Parse() throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
	  Jws<Claims> re = Jwts.parser().setSigningKey(SECRET_KEY.getBytes("UTF-8")).parseClaimsJws(test_jwt);
	}
}
