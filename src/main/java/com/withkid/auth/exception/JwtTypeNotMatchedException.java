package com.withkid.auth.exception;

import io.jsonwebtoken.JwtException;

public class JwtTypeNotMatchedException extends JwtException{

	public JwtTypeNotMatchedException() {
		super("토큰의 타입이 다릅니다.");
	}

}