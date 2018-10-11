package com.depromeet.team5.jwt;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	final private String SECRET_KEY = "depromeet_mini_prj";
	final private Date exp = new Date(System.currentTimeMillis() + 72000000);
	
	public String create(HashMap<String, Object> claims) {
		return Jwts.builder()
				   .setHeaderParam("typ", "JWT")
				   .setHeaderParam("regDate", System.currentTimeMillis())
				   .setSubject("email")
				   .setClaims(claims)
				   .setExpiration(exp)
				   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				   .compact();
	}
	
	public boolean isUsable(String jwt) {
		try {
			Jws<Claims> re = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
			System.out.println(re.toString());
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
