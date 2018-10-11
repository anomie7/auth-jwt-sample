package com.depromeet.team5.jwt;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.depromeet.team5.User;
import com.depromeet.team5.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	final private String SECRET_KEY = "depromeet_mini_prj";
	final private Date exp = new Date(System.currentTimeMillis() + 72000000);
	@Autowired
	private UserRepository userRepository;

	public String create(HashMap<String, Object> claims) {
		return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
				.setSubject("email").setClaims(claims).setExpiration(exp).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public boolean isUsable(String jwt) {
		try {
			Jws<Claims> re = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
			System.out.println(re.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
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

	public ResponseEntity<String> userHandler(User user) throws Exception {
		String jwt = null;

		if (userRepository.findByEmail(user.getEmail()) == null) {
			HashMap<String, Object> claims = new HashMap<>();
			claims.put("email", user.getEmail());
			jwt = this.create(claims);
			userRepository.save(user);
		}

		if (!this.isUsable(jwt)) {
			// refresh token
			throw new Exception("무효한 토큰입니다.");
		}

		return  ResponseEntity.ok().header("Authentication", jwt).body("Done");
	}
}
