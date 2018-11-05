package com.depromeet.team5.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.depromeet.team5.exception.JwtTypeNotMatchedException;
import com.depromeet.team5.exception.RefreshTokenExpireDateUpdatePeriodException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	final private String SECRET_KEY = "depromeet_mini_prj";
	final private ZonedDateTime exp = LocalDateTime.now().atZone(ZoneId.systemDefault());

	public String createAccessToken(HashMap<String, Object> claims) {
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setHeaderParam("type", "access-token").setSubject("User-Identity").setClaims(claims)
				.setExpiration(Date.from(exp.plusHours(1).toInstant())).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public String createRefreshToken(HashMap<String, Object> claims) {
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setHeaderParam("type", "refresh-token").setClaims(claims)
				.setExpiration(Date.from(exp.plusMonths(1).toInstant())).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public String updateRefreshToken(String token) {
		Jws<Claims> re = this.getBody(token);
		Object email = re.getBody().get("email");
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("email", email);
		Object regDate = re.getHeader().get("regDate");
		Instant exp = LocalDateTime.ofInstant(re.getBody().getExpiration().toInstant(), ZoneId.systemDefault())
				.plusMonths(1).atZone(ZoneId.systemDefault()).toInstant();
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", regDate)
				.setHeaderParam("type", "refresh-token").setClaims(claims).setExpiration(Date.from(exp))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public boolean thisAccessTokenUsable(String jwt) throws ExpiredJwtException {
		Jws<Claims> re = getBody(jwt);
		if (!re.getHeader().get("type").equals("access-token")) {
			throw new JwtTypeNotMatchedException();
		}
		System.out.println(re.toString());
		return true;
	}

	public boolean thisRefreshTokenUsable(String jwt) throws ExpiredJwtException {
		Jws<Claims> re = getBody(jwt);
		if (!re.getHeader().get("type").equals("refresh-token")) {
			throw new JwtTypeNotMatchedException();
		}
		LocalDateTime expDate = LocalDateTime.ofInstant(re.getBody().getExpiration().toInstant(),
				ZoneId.systemDefault());
		if (LocalDateTime.now().isAfter(expDate.minusWeeks(1))) {
			throw new RefreshTokenExpireDateUpdatePeriodException();
		}
		System.out.println(re.toString());
		return true;
	}

	public Jws<Claims> getBody(String jwt) {
		try {
			Jws<Claims> re = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
			System.out.println(re.toString());
			return re;
		} catch (Exception e) {
			return null;
		}
	}

	public String getEmail(ResponseEntity<String> response) {
		Jws<Claims> re = getBody(response.getHeaders().get("Authentication").get(0));
		return (String) re.getBody().get("email");
	}
}
