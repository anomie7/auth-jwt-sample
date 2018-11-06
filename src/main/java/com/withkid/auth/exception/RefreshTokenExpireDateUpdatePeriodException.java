package com.withkid.auth.exception;

import io.jsonwebtoken.JwtException;

public class RefreshTokenExpireDateUpdatePeriodException extends JwtException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefreshTokenExpireDateUpdatePeriodException() {
		super("리프레쉬 토큰이 곧 만료될 예정입니다.");
	}

}
