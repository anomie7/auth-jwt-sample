package com.withkid.auth.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.withkid.auth.exception.JwtTypeNotMatchedException;
import com.withkid.auth.exception.RefreshTokenUpdatePeriodOverException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	final private String SECRET_KEY = "depromeet_mini_prj";

	public boolean thisAccessTokenUsable(String jwt) throws ExpiredJwtException {
		Jws<Claims> body = getBody(jwt);
		if (!body.getHeader().get("type").equals("access-token")) {
			throw new JwtTypeNotMatchedException();
		}
		return true;
	}

	public boolean thisRefreshTokenUsable(String jwt) throws Exception {
		Jws<Claims> body = getBody(jwt);
		if (!body.getHeader().get("type").equals("refresh-token")) {
			throw new JwtTypeNotMatchedException();
		}
		LocalDateTime expDate = LocalDateTime.ofInstant(getExpirationDate(body), ZoneId.systemDefault());
		if (LocalDateTime.now().isAfter(expDate.minusWeeks(1))) {
			throw new RefreshTokenUpdatePeriodOverException();
		}
		return true;
	}

	public String updateRefreshToken(String token) {
		Jws<Claims> body = this.getBody(token);
		HashMap<String, Object> claims = getClaims(body);
		Object regDate = body.getHeader().get("regDate");
		Instant expDatePlusOneMonth = ZonedDateTime.ofInstant(getExpirationDate(body), ZoneId.systemDefault())
				.plusMonths(1).toInstant();
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", regDate)
				.setHeaderParam("type", "refresh-token").setClaims(claims).setExpiration(Date.from(expDatePlusOneMonth))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Jws<Claims> getBody(String jwt) {
		Jws<Claims> body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
		return body;
	}

	private HashMap<String, Object> getClaims(Jws<Claims> body) {
		HashMap<String, Object> claims = new HashMap<>();
		Object email = body.getBody().get("email");
		claims.put("email", email);
		return claims;
	}

	private Instant getExpirationDate(Jws<Claims> body) {
		Instant exp = body.getBody().getExpiration().toInstant();
		return exp;
	}

	public String createAccessToken(HashMap<String, Object> claims) {
		ZonedDateTime exp = LocalDateTime.now().atZone(ZoneId.systemDefault());
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setHeaderParam("type", "access-token").setSubject("User-Identity").setClaims(claims)
				.setExpiration(Date.from(exp.plusHours(1).toInstant())).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public String createRefreshToken(HashMap<String, Object> claims) {
		ZonedDateTime exp = LocalDateTime.now().atZone(ZoneId.systemDefault());
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setHeaderParam("type", "refresh-token").setClaims(claims)
				.setExpiration(Date.from(exp.plusMonths(1).toInstant())).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public String createSampleRefreshToken(Date exp) {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", "sample@sample.com");
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setHeaderParam("type", "refresh-token").setClaims(claims).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
}
